package com.vocahype.controller;


import com.vocahype.dto.ResponseEntity;
import com.vocahype.entity.Word;
import com.vocahype.service.SynonymService;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SynonymController {
    private final SynonymService synonymService;

//    @GetMapping(value = Routing.WORD_ID)
//    public ResponseEntity getWord(@PathVariable Long wordId) {
//        Word word = wordService.getWordById(wordId);
//        return ResponseEntity.of(word, Map.of(
//                "pos", ResponseEntity.of(word.getPos()),
//                "definition", word.getDefinitions().stream().map(definition -> ResponseEntity.of(definition, Map.of(
//                        "examples", ResponseEntity.of(definition.getExamples())
//                )))
//        ));
//    }
//
//    @GetMapping(value = Routing.WORD)
//    public ResponseEntity searchWords(@RequestParam(name = "search") String word) {
//        return ResponseEntity.of(wordService.getWordsByWord(word));
//    }
}
