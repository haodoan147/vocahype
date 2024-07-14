package com.vocahype.controller;

import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.service.WordUserKnowledgeService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WordUserKnowledgeController {
    private final WordUserKnowledgeService wordUserKnowledgeService;

    @GetMapping(value = Routing.KNOWLEDGE_TEST_50)
    public ResponseEntityJsonApi get50WordForUserKnowledge() {
//        return ResponseEntity.of(wordUserKnowledgeService.get50WordForUserKnowledge());
        return new ResponseEntityJsonApi(wordUserKnowledgeService.get50WordForUserKnowledge());
    }

    @PostMapping(Routing.KNOWLEDGE_TEST_50)
    public Map<String, Object> saveUserKnowledge(@RequestBody List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        return wordUserKnowledgeService.saveUserKnowledge(wordUserKnowledgeDTO);
    }

    // Dedicated endpoint for the frontend to get the list of words for the user to learn
    @GetMapping(value = Routing.WORD_USER_KNOWLEDGE)
    public ResponseEntityJsonApi getListWordUserKnowledge() {
//        return ResponseEntity.of(wordUserKnowledgeService.getListWordUserKnowledge());
        return new ResponseEntityJsonApi(wordUserKnowledgeService.getListWordUserKnowledge());
    }

    // Dedicated endpoint for the frontend to get the list of words for the user to learn
    @PutMapping(Routing.WORD_USER_KNOWLEDGE)
    public void resetUserKnowledge() {
        wordUserKnowledgeService.resetUserKnowledge();
    }
}
