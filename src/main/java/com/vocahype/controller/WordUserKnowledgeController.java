package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.service.WordUserKnowledgeService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordUserKnowledgeController {
    private final WordUserKnowledgeService wordService;

    @GetMapping(value = Routing.KNOWLEDGE_TEST_50)
    public ResponseEntity get50WordForUserKnowledge() {
        return ResponseEntity.of(wordService.get50WordForUserKnowledge());
    }

    @GetMapping(value = Routing.KNOWLEDGE_TEST)
    public ResponseEntity getListWordUserKnowledge() {
        return ResponseEntity.of(wordService.getListWordUserKnowledge());
    }

    @PostMapping(Routing.KNOWLEDGE_TEST_50)
    public void checkUserKnowledge(@RequestBody List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        wordService.checkUserKnowledge(wordUserKnowledgeDTO);
    }

    @PutMapping(Routing.KNOWLEDGE_TEST)
    public void resetUserKnowledge() {
        wordService.resetUserKnowledge();
    }
}
