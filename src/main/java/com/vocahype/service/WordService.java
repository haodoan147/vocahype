package com.vocahype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.DefinitionDTO;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.WordStatus;
import com.vocahype.entity.Word;
import com.vocahype.entity.Meaning;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.MeaningRepository;
import com.vocahype.repository.PosRepository;
import com.vocahype.repository.SynonymRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;
    private final SynonymRepository synonymRepository;
    private final ObjectMapper objectMapper;
    private final MeaningRepository meaningRepository;
    private final PosRepository posRepository;

    public WordDTO getWordById(Long id) {
        WordDTO word = wordRepository.findWordDTOById(id, SecurityUtil.getCurrentUserId()).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
        if (word.getMeanings() != null) {
            word.getMeanings().forEach(meaningDTO -> {
                Set<SynonymDTO> synonym = meaningDTO.getSynonyms();
                synonym.addAll(synonymRepository.findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(id, true));
                synonym.addAll(synonymRepository.findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(id, false));
                meaningDTO.setSynonyms(synonym);
            });
        }
        return word;
    }

    public Page<WordDTO> getWordsByWord(String word, boolean exact, final int page, final int size, final String status) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = SecurityUtil.getCurrentUserId();
        if (status != null && !status.equalsIgnoreCase("TO_LEARN") && !status.isBlank()) {
            try {
                List<Integer> levelList = WordStatus.valueOf(status.toUpperCase()).getLevelList();
                if (exact) return wordRepository.findByWordIgnoreCaseAndUserWordComprehensionsOrderById(word, userId, levelList, pageable);
                return wordRepository.findByWordContainsIgnoreCaseAndUserWordComprehensionsOrderById(word, userId, levelList, pageable);
            } catch (IllegalArgumentException e) {
                throw new InvalidException("Invalid param", "Status must be one of: " + Arrays.toString(WordStatus.values()));
            }
        }
        if (exact) return wordRepository.findByWordIgnoreCaseOrderById(word, userId, pageable);
        return wordRepository.findByWordContainsIgnoreCaseOrderById(word, userId, pageable);
    }

    public long countWord(final String word, final boolean exact) {
        if (exact) return wordRepository.countByWordIgnoreCase(word);
        return wordRepository.countByWordContainsIgnoreCase(word);
    }

    @Transactional
    public WordDTO updateWord(final Long wordId, final JsonNode jsonNode) {
        Word resourceWord = objectMapper.convertValue(jsonNode.get("data").get(0).get("attributes"), Word.class);

        Word targetWord;

        if (wordId == null) {
            wordRepository.findByWord(resourceWord.getWord()).ifPresent(word -> {
                throw new InvalidException("Word already exist", "Word already exist with word: " + resourceWord.getWord());
            });
            targetWord = Word.builder().word(resourceWord.getWord()).meanings(new HashSet<>()).build();
        } else {
            targetWord = wordRepository.findById(wordId).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with wordId: " + wordId));
        }

        if (resourceWord.getCount() != null && !resourceWord.getCount().equals(targetWord.getCount())) {
            targetWord.setCount(resourceWord.getCount());
        }
        if (resourceWord.getPoint() != null && !resourceWord.getPoint().equals(targetWord.getPoint())) {
            targetWord.setPoint(resourceWord.getPoint());
        }
        if (resourceWord.getPhonetic() != null && !resourceWord.getPhonetic().equals(targetWord.getPhonetic())) {
            targetWord.setPhonetic(resourceWord.getPhonetic());
        }
        if (resourceWord.getSyllable() != null && !resourceWord.getSyllable().equals(targetWord.getSyllable())) {
            targetWord.setSyllable(resourceWord.getSyllable());
        }
        if (resourceWord.getPhoneticStart() != null && !resourceWord.getPhoneticStart().equals(targetWord.getPhoneticStart())) {
            targetWord.setPhoneticStart(resourceWord.getPhoneticStart());
        }
        if (resourceWord.getPhoneticEnd() != null && !resourceWord.getPhoneticEnd().equals(targetWord.getPhoneticEnd())) {
            targetWord.setPhoneticEnd(resourceWord.getPhoneticEnd());
        }
        if (wordId == null) {
            targetWord = wordRepository.saveAndFlush(targetWord);
        }
        Set<Meaning> meanings = new HashSet<>(targetWord.getMeanings());
        Word finalTargetWord = targetWord;
        jsonNode.get("included").forEach(included -> {
            if (included.get("type").asText().equals("meaning")) {
                Meaning meaning;
                boolean isMeaningExist = included.has("id");
                if (isMeaningExist) {
                    meaning = meaningRepository.findById(included.get("id").asLong())
                            .orElseThrow(() -> new InvalidException("Meaning not found", "Not found any meaning with id: " + included.get("id").asLong()));
                    if (!meaning.getWord().getId().equals(finalTargetWord.getId())) {
                        throw new InvalidException("Meaning not found", "Meaning with id: " + meaning.getId()+ " not belong to current word with id: " + finalTargetWord.getId());
                    }
                } else {
                    meaning = Meaning.builder().word(finalTargetWord).build();
                }
                if (!isMeaningExist && !included.get("relationships").has("pos")) {
                    throw new InvalidException("Pos not found", "Pos is required for new meaning");
                }
                if (included.get("relationships").has("pos")) {
                    JsonNode dataPos = included.get("relationships").get("pos").get("data");
                    if (!dataPos.has("id")) {
                        throw new InvalidException("Pos not found", "Pos id is required for new meaning");
                    }
                    if (!isMeaningExist || !dataPos.get("id").asText().equals(meaning.getPos().getPosTag())) {
                        meaning.setPos(posRepository.findById(dataPos.get("id")
                                .asText()).orElseThrow(() -> new InvalidException("Pos not found",
                                "Not found any pos with wordId: "
                                        + dataPos.get("id").asText())));
                    }
                }

                Set<DefinitionDTO> definitionDTOs = new HashSet<>();
                if (included.get("attributes").has("definitions")) {
                    included.get("attributes").get("definitions").forEach(definition -> {
                        DefinitionDTO definitionDTO = objectMapper.convertValue(definition, DefinitionDTO.class);
                        definitionDTOs.add(definitionDTO);
                    });
                }
                meaning.setDefinitions(definitionDTOs);
                meanings.add(meaning);
            }
        });
        meaningRepository.saveAllAndFlush(meanings);
        targetWord.setMeanings(meanings);

        return new WordDTO(targetWord, true, true);
    }

    public void deleteWord(final Long wordId) {
        wordRepository.findById(wordId)
                .ifPresent(wordRepository::delete);
    }
}
