package com.vocahype.controller;

import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.service.UserWordComprehensionService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserWordComprehensionController {
    private final UserWordComprehensionService userWordComprehensionService;

    @PostMapping(value = Routing.LEARNING_EASY)
    public void getWord(@PathVariable Long wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.EASY);
    }

    @PostMapping(value = Routing.LEARNING_HARD)
    public void getWordHard(@PathVariable Long wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.HARD);
    }

    @PostMapping(value = Routing.LEARNING_NORMAL)
    public void getWordNormal(@PathVariable Long wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.NORMAL);
    }

    @PostMapping(value = Routing.LEARNING_MASTERED)
    public void getWordMastered(@PathVariable Long wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.MASTERED);
    }

    @PostMapping(value = Routing.LEARNING_IGNORE)
    public void getWordIgnore(@PathVariable Long wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.IGNORE);
    }
}
