package com.vocahype.service;

import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.WordStatus;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.SynonymRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;
    private final SynonymRepository synonymRepository;

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

    public List<WordDTO> getWordsByWord(String word, boolean exact, final int page, final int size, final String status) {
        Pageable pageable = PageRequest.of(page, size);
        String userId = SecurityUtil.getCurrentUserId();
        if (status != null && !status.equalsIgnoreCase("TO_LEARN")) {
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
}
