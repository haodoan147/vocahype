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

import java.util.HashSet;
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
//        Word word = wordRepository.findById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
//        List<SynonymDTO> collect = word.getSynonyms() == null ? null : word.getSynonyms().stream().map(synonym -> new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym())).collect(Collectors.toList());
//        WordDTO wordDTO = modelMapper.map(word, WordDTO.class);
//        wordDTO.setSynonyms(collect);
//        List<DefinitionDTO> definitionDTOS = word.getDefinitions() == null ? null : word.getDefinitions().stream().map(definition -> new DefinitionDTO(definition.getId(), definition.getDefinition(), definition.getExamples().stream().map(example -> new ExampleDTO(example.getId(), example.getExample())).collect(Collectors.toSet()))).collect(Collectors.toList());
//        wordDTO.setDefinitions(definitionDTOS);
//        return wordDTO;
        WordDTO word = wordRepository.findWordDTOById(id).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + id));
//        WordDTO wordDTO = modelMapper.map(word, WordDTO.class);
//        wordDTO.setSynonyms(word.getSynonyms() == null ? null : word.getSynonyms().stream().map(SynonymDTO::new).collect(Collectors.toSet()));
        // ex: one = match ->
        // wordId = one (already if eager fetch)
        // synonymId = one
        // wordId = match
        // synonymId = match
        Set<SynonymDTO> synonym = word.getSynonyms();
        synonym.addAll(synonymRepository.findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(id, true));
        synonym.addAll(synonymRepository.findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(id, false));
//        synonymOfSynonym.forEach(synonym1 -> {
//            List<SynonymDTO> synonymDTOBySynonymIDWordIdAndIsSynonym = synonymRepository.findSynonymDTOBySynonymID_WordIdAndIsSynonym(synonym1.getId(), true);
//            synonym.addAll(synonymDTOBySynonymIDWordIdAndIsSynonym);
////            synonymDTOBySynonymIDWordIdAndIsSynonym.forEach(synonymDTO -> {
////                synonym.addAll(synonymRepository.findSynonymDTOBySynonymID_WordIdAndIsSynonym(synonymDTO.getId(), true));
////            });
//            List<SynonymDTO> synonymDTOBySynonymIDWordIdAndIsSynonym1 = synonymRepository.findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(synonym1.getId(), true);
//            synonym.addAll(synonymDTOBySynonymIDWordIdAndIsSynonym1);
//        });
//        synonym.stream().distinct().filter(synonymDTO -> !synonymDTO.getId().equals(id));
        word.setSynonyms(synonym);
        return word;
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
