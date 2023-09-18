package com.vocahype.controller;


import com.vocahype.service.SynonymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
