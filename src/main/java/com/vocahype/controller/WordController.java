package com.vocahype.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.request.searchwordsapi.Result;
import com.vocahype.dto.request.wordsapi.WordData;
import com.vocahype.exception.InvalidException;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD_ID)
    public ResponseEntityJsonApi getWord(@PathVariable Long wordId) {
        return new ResponseEntityJsonApi(wordService.getWordById(wordId));
    }

    @GetMapping(value = Routing.WORD_BY_WORD)
    public WordData getWordByWord(@PathVariable String word) {
        return wordService.getWordByWord(word);
    }

    @GetMapping(value = Routing.WORD)
    public Result searchWords(@RequestParam(name = "search") String word,
                              @RequestParam(name = "exact") boolean exact,
                              @RequestParam(name = "status", required = false) String status,
                              @RequestParam(name = "page[offset]") int offset,
                              @RequestParam(name = "page[limit]") int limit) {
        if (offset <= 0 || limit <= 0) {
            throw new InvalidException("Invalid param", "Offset and limit must be greater than 0!");
        }
        return wordService.getWordsByWord(word, exact, offset - 1, limit, status);
//        long countWord = wordsByWord.getTotalElements();
//        return new ResponseEntityJsonApi(wordsByWord.getContent(),
//                new MetaResponseEntity(1, ConversionUtils.roundUp(countWord, limit), offset, limit, countWord));
    }

    @PutMapping(value = Routing.WORD_ID)
    public ResponseEntityJsonApi updateWord(@PathVariable String wordId, @RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(wordService.updateWord(wordId, jsonNode));
    }

    @PostMapping(value = Routing.WORD)
    public ResponseEntityJsonApi updateWord(@RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(wordService.updateWord(null, jsonNode));
    }

    @DeleteMapping(value = Routing.WORD_ID)
    public void deleteWord(@PathVariable String wordId) {
        wordService.deleteWord(wordId);
    }
}
