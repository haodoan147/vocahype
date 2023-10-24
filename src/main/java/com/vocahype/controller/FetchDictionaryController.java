package com.vocahype.controller;


import com.vocahype.service.FetchDictionaryService;
import com.vocahype.service.SynonymService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FetchDictionaryController {
    private final FetchDictionaryService fetchDictionaryService;

    @GetMapping(value = Routing.FETCH_DICTIONARY)
    public void getWord(@PathVariable String word) {
        fetchDictionaryService.fetchDictionary(word);
    }
//
//    @GetMapping(value = Routing.WORD)
//    public ResponseEntity searchWords(@RequestParam(name = "search") String word) {
//        return ResponseEntity.of(wordService.getWordsByWord(word));
//    }
}
