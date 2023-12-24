package com.vocahype.controller;

import com.vocahype.dto.MetaResponseEntity;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.WordDTO;
import com.vocahype.exception.InvalidException;
import com.vocahype.service.WordService;
import com.vocahype.util.ConversionUtils;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        List<WordDTO> wordsByWord = wordService.getWordsByWord(word, exact, offset - 1, limit, status);
        long countWord = wordService.countWord(word, exact);
        return new ResponseEntityJsonApi(wordsByWord,
                new MetaResponseEntity(1, ConversionUtils.roundUp(countWord, limit), offset, limit, countWord));
    }
}
