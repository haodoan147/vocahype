package com.vocahype.controller;

import com.vocahype.dto.ResponseEntity;
import com.vocahype.dto.ResponseEntityJsonApi;
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
    public ResponseEntityJsonApi get50WordForUserKnowledge() {
//        return ResponseEntity.of(wordUserKnowledgeService.get50WordForUserKnowledge());
        return ResponseEntityJsonApi.response(wordUserKnowledgeService.get50WordForUserKnowledge());
    }

    @PostMapping(Routing.KNOWLEDGE_TEST_50)
    public void saveUserKnowledge(@RequestBody List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        wordUserKnowledgeService.saveUserKnowledge(wordUserKnowledgeDTO);
    }

    @GetMapping(value = Routing.WORD_USER_KNOWLEDGE)
    public ResponseEntityJsonApi getListWordUserKnowledge() {
//        return ResponseEntity.of(wordUserKnowledgeService.getListWordUserKnowledge());
        return ResponseEntityJsonApi.response(wordUserKnowledgeService.getListWordUserKnowledge());
    }

    @PutMapping(Routing.WORD_USER_KNOWLEDGE)
    public void resetUserKnowledge() {
        wordUserKnowledgeService.resetUserKnowledge();
    }
}
