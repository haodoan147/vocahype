package com.vocahype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.WordStatus;
import com.vocahype.entity.Word;
import com.vocahype.entity.Meaning;
import com.vocahype.exception.InvalidException;
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
    public WordDTO updateWord(final Long id, final JsonNode jsonNode) {
        Word targetWord = wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));

        Word resourceWord = objectMapper.convertValue(jsonNode.get("data").get(0).get("attributes"), Word.class);

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
        if (jsonNode.get("data").get(0).get("relationships").has("meanings")
                && jsonNode.get("data").get(0).get("relationships").get("meanings").get("data").size() != targetWord.getMeanings().size()) {
            Set<Meaning> meanings = new HashSet<>();
            jsonNode.get("data").get(0).get("relationships").get("meanings").get("data").forEach(meaning -> {
                Long meaningId = meaning.get("id").asLong();
                meanings.add(Meaning.builder().id(meaningId).build());
            });
            targetWord.setMeanings(meanings);
        }

        return new WordDTO(targetWord);
    }
}
