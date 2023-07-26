package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.dto.WordDTO;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.KNOWLEDGE_TEST_50)
    public ResponseEntity get50WordForUserKnowledge() {
        return ResponseEntity.of(wordService.getWordList());
    }

    @GetMapping(value = Routing.KNOWLEDGE_TEST)
    public ResponseEntity getListWordUserKnowledge() {
        return ResponseEntity.of(wordService.getListWordUserKnowledge());
    }

    @PostMapping(Routing.KNOWLEDGE_TEST)
    public void checkUserKnowledge(@RequestBody List<WordDTO> wordDTO) {
        wordService.checkUserKnowledge(wordDTO);
    }

    @PutMapping(Routing.KNOWLEDGE_TEST)
    public void resetUserKnowledge() {
        wordService.resetUserKnowledge();
    }
}
