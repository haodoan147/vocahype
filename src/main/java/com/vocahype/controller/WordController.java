package com.vocahype.controller;

import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD_ID)
    public ResponseEntityJsonApi getWord(@PathVariable Long wordId) {
//        WordDTO word = wordService.getWordById(wordId);
////        return ResponseEntity.of(word, ResponseEntity.mapOfNullable(
////                "pos", word.getPos(),
////                "definition", word.getDefinitions().stream().map(definition -> ResponseEntity.of(definition, ResponseEntity.mapOfNullable(
////                        "examples", definition.getExamples())
////                )).collect(Collectors.toList()),
////                "synonyms", word.getSynonyms().stream().map(synonym -> ResponseEntity.of(new SynonymDTO(synonym.getSynonym().getId(), synonym.getSynonym().getWord(), synonym.getIsSynonym()))).collect(Collectors.toList())
////        ));
//        ResponseEntityJsonApi responseEntityJsonApi = new ResponseEntityJsonApi();
//        responseEntityJsonApi.add(word);
//        return responseEntityJsonApi.sort();
        return new ResponseEntityJsonApi(wordService.getWordById(wordId));
    }

    @GetMapping(value = Routing.WORD)
    public ResponseEntityJsonApi searchWords(@RequestParam(name = "search") String word) {
////        return ResponseEntity.of(wordService.getWordsByWord(word));
//        ResponseEntityJsonApi responseEntityJsonApi = new ResponseEntityJsonApi();
//        responseEntityJsonApi.add(wordService.getWordsByWord(word));
//        return responseEntityJsonApi.sort();
        return new ResponseEntityJsonApi(wordService.getWordsByWord(word));
    }
}
