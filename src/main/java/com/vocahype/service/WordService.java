package com.vocahype.service;

import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.SynonymRepository;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;
    private final SynonymRepository synonymRepository;

    public WordDTO getWordById(Long id) {
        WordDTO word = wordRepository.findWordDTOById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
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

    public List<WordDTO> getWordsByWord(String word, boolean exact, final int page, final int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (exact) return wordRepository.findByWordIgnoreCaseOrderById(word, pageable);
        return wordRepository.findByWordContainsIgnoreCaseOrderById(word, pageable).stream().map((element) -> {
//            WordDTO wordDTO = modelMapper.map(element, WordDTO.class);
            WordDTO wordDTO = new WordDTO(element);
//            element.getMeanings().forEach(meaning -> {
//                wordDTO.getMeanings(meaning.getSynonyms() == null ? null : meaning.getSynonyms().stream().map(SynonymDTO::new).collect(Collectors.toSet()));
//
//            });
            return wordDTO;
        }).collect(Collectors.toList());
    }

    public long countWord(final String word, final boolean exact) {
        if (exact) return wordRepository.countByWordIgnoreCase(word);
        return wordRepository.countByWordContainsIgnoreCase(word);
    }
}
