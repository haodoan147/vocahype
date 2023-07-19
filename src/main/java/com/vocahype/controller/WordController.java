package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD, produces = "application/json")
    public ResponseEntity getMeterIntegrity() {
        return ResponseEntity.of(wordService.getWordList());
    }
}
