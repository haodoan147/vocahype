package com.vocahype.service;

import com.vocahype.dto.DefinitionDTO;
import com.vocahype.dto.ExampleDTO;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;

    public WordDTO getWordById(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
        List<SynonymDTO> collect = word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym())).collect(Collectors.toList());
        WordDTO wordDTO = modelMapper.map(word, WordDTO.class);
        wordDTO.setSynonyms(collect);
        List<DefinitionDTO> definitionDTOS = word.getDefinitions() == null ? null : word.getDefinitions().stream().map(definition -> new DefinitionDTO(definition.getId(), definition.getDefinition(), definition.getExamples().stream().map(example -> new ExampleDTO(example.getId(), example.getExample())).collect(Collectors.toSet()))).collect(Collectors.toList());
        wordDTO.setDefinitions(definitionDTOS);
        return wordDTO;
    }

    public List<Word> getWordsByWord(String word) {
        return wordRepository.findByWordContainsIgnoreCase(word);
    }
}
