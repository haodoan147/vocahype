package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.entity.Word;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD_ID)
    public ResponseEntity getWord(@PathVariable Long wordId) {
        Word word = wordService.getWordById(wordId);
        return ResponseEntity.of(word, ResponseEntity.mapOfNullable(
                "pos", word.getPos(),
                "definition", word.getDefinitions().stream().map(definition -> ResponseEntity.of(definition, ResponseEntity.mapOfNullable(
                        "examples", definition.getExamples())
                )).collect(Collectors.toList()),
                "synonyms", word.getSynonyms().stream().map(synonym -> ResponseEntity.of(new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym()))).collect(Collectors.toList())
        ));
    }

    @GetMapping(value = Routing.WORD)
    public ResponseEntity searchWords(@RequestParam(name = "search") String word) {
        return ResponseEntity.of(wordService.getWordsByWord(word));
    }
}
