package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.entity.Word;
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
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD_ID)
    public ResponseEntity get50WordForUserKnowledge(@PathVariable Long wordId) {
        Word word = wordService.getWordById(wordId);
        return ResponseEntity.of(word, Map.of("pos", ResponseEntity.of(word.getPos())));
    }

    @GetMapping(value = Routing.WORD)
    public ResponseEntity searchWord(@RequestParam(name = "search") String word) {
        return ResponseEntity.of(wordService.getWordByWord(word));
    }
}
