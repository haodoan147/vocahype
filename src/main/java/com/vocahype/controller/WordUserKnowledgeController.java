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
    private final WordUserKnowledgeService wordUserKnowledgeService;

    @GetMapping(value = Routing.KNOWLEDGE_TEST_50)
    public ResponseEntity get50WordForUserKnowledge() {
        return ResponseEntity.of(wordUserKnowledgeService.get50WordForUserKnowledge());
    }

    @PostMapping(Routing.KNOWLEDGE_TEST_50)
    public void saveUserKnowledge(@RequestBody List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        wordUserKnowledgeService.saveUserKnowledge(wordUserKnowledgeDTO);
    }

    @GetMapping(value = Routing.WORD_USER_KNOWLEDGE)
    public ResponseEntity getListWordUserKnowledge() {
        return ResponseEntity.of(wordUserKnowledgeService.getListWordUserKnowledge());
    }

    @PutMapping(Routing.WORD_USER_KNOWLEDGE)
    public void resetUserKnowledge() {
        wordUserKnowledgeService.resetUserKnowledge();
    }
}
