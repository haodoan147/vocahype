package com.vocahype.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.dto.MetaResponseEntity;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.WordDTO;
import com.vocahype.exception.InvalidException;
import com.vocahype.service.WordService;
import com.vocahype.util.ConversionUtils;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD_ID)
    public ResponseEntityJsonApi getWord(@PathVariable Long wordId) {
        return new ResponseEntityJsonApi(wordService.getWordById(wordId));
    }

    @GetMapping(value = Routing.WORD)
    public ResponseEntityJsonApi searchWords(@RequestParam(name = "search") String word,
                                             @RequestParam(name = "exact") boolean exact,
                                             @RequestParam(name = "status", required = false) String status,
                                             @RequestParam(name = "page[offset]") int offset,
                                             @RequestParam(name = "page[limit]") int limit) {
        if (offset <= 0 || limit <= 0) {
            throw new InvalidException("Invalid param", "Offset and limit must be greater than 0!");
        }
        Page<WordDTO> wordsByWord = wordService.getWordsByWord(word, exact, offset - 1, limit, status);
        long countWord = wordsByWord.getTotalElements();
        return new ResponseEntityJsonApi(wordsByWord.getContent(),
                new MetaResponseEntity(1, ConversionUtils.roundUp(countWord, limit), offset, limit, countWord));
    }

    @PutMapping(value = Routing.WORD_ID)
    public ResponseEntityJsonApi updateWord(@PathVariable Long wordId, @RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(wordService.updateWord(wordId, jsonNode));
    }

    @PostMapping(value = Routing.WORD)
    public ResponseEntityJsonApi updateWord(@RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(wordService.updateWord(null, jsonNode));
    }

    @DeleteMapping(value = Routing.WORD_ID)
    public void deleteWord(@PathVariable Long wordId) {
        wordService.deleteWord(wordId);
    }
}
