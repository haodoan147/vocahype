package com.vocahype.service;

import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;

    public WordDTO getWordById(Long id) {
//        Word word = wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
//        List<SynonymDTO> collect = word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym())).collect(Collectors.toList());
//        WordDTO wordDTO = modelMapper.map(word, WordDTO.class);
//        wordDTO.setSynonyms(collect);
//        List<DefinitionDTO> definitionDTOS = word.getDefinitions() == null ? null : word.getDefinitions().stream().map(definition -> new DefinitionDTO(definition.getId(), definition.getDefinition(), definition.getExamples().stream().map(example -> new ExampleDTO(example.getId(), example.getExample())).collect(Collectors.toSet()))).collect(Collectors.toList());
//        wordDTO.setDefinitions(definitionDTOS);
//        return wordDTO;
        Word word = wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
        WordDTO wordDTO = modelMapper.map(word, WordDTO.class);
        wordDTO.setSynonyms(word.getSynonyms() == null ? null : word.getSynonyms().stream().map(SynonymDTO::new).collect(Collectors.toSet()));
        return wordDTO;
    }

    public List<WordDTO> getWordsByWord(String word, boolean exact, final int page, final int size) {
//        List<Word> words = wordRepository.findByWordContainsIgnoreCase(word);
//        List<SynonymDTO> collect = words.stream().map(word1 -> word1.getSynonyms() == null ? null : word1.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym())).collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());
//        List<WordDTO> wordDTOS = words.stream().map(word1 -> modelMapper.map(word1, WordDTO.class)).collect(Collectors.toList());
//        for (int i = 0; i < wordDTOS.size(); i++) {
//            wordDTOS.get(i).setSynonyms(collect.stream().filter(synonymDTO -> synonymDTO.getWordId().equals(wordDTOS.get(i).getId())).collect(Collectors.toList()));
//        }
        Pageable pageable = PageRequest.of(page, size);
        if (exact) return wordRepository.findByWordIgnoreCase(word, pageable);
        return wordRepository.findByWordContainsIgnoreCase(word, pageable).stream().map((element) -> {
            WordDTO wordDTO = modelMapper.map(element, WordDTO.class);
            wordDTO.setSynonyms(element.getSynonyms() == null ? null : element.getSynonyms().stream().map(SynonymDTO::new).collect(Collectors.toSet()));
            return wordDTO;
        }).collect(Collectors.toList());
    }

    public long countWord(final String word, final boolean exact) {
        if (exact) return wordRepository.countByWordIgnoreCase(word);
        return wordRepository.countByWordContainsIgnoreCase(word);
    }
}
