package com.vocahype.controller;

import com.vocahype.dto.*;
import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.exception.InvalidException;
import com.vocahype.service.UserWordComprehensionService;
import com.vocahype.util.ConversionUtils;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserWordComprehensionController {
    private final UserWordComprehensionService userWordComprehensionService;

    @PostMapping(value = Routing.LEARNING_EASY)
    public void getWord(@PathVariable String wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.EASY);
    }

    @PostMapping(value = Routing.LEARNING_HARD)
    public void getWordHard(@PathVariable String wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.HARD);
    }

    @PostMapping(value = Routing.LEARNING_NORMAL)
    public void getWordNormal(@PathVariable String wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.NORMAL);
    }

    @PostMapping(value = Routing.LEARNING_MASTERED)
    public void getWordMastered(@PathVariable String wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.MASTERED);
    }

    @PostMapping(value = Routing.LEARNING_IGNORE)
    public void getWordIgnore(@PathVariable String wordId) {
        userWordComprehensionService.saveWordUserKnowledge(wordId, Assessment.IGNORE);
    }

    @GetMapping(value = Routing.WORDS_LEARN)
    public FrequencyResponseDTO getLearningWord(@RequestParam(name = "page[offset]") int offset,
                                                @RequestParam(name = "page[limit]") int limit,
                                                @RequestParam(name = "filter[topicId]", required = false) Long topicId) {
        if (offset <= 0 || limit <= 0) {
            throw new InvalidException("Invalid param", "Offset and limit must be greater than 0!");
        }
        return userWordComprehensionService.getWordTest(offset - 1, limit, topicId);
    }

    @PutMapping(value = Routing.WORDS_DELAY)
    public void delayLearningWord(@PathVariable String wordId, @RequestParam(name = "day") int day) {
        if (day <= 0) throw new InvalidException("Invalid param", "Day must be greater than 0!");
        userWordComprehensionService.delayLearningWord(wordId, day);
    }

    @DeleteMapping(value = Routing.RESET_LEARNING_PROGRESSION)
    public void resetLearningProgression() {
        userWordComprehensionService.resetLearningProgression();
    }

    @DeleteMapping(value = Routing.RESET_LEARNING_PROGRESSION_WORD_ID)
    public void resetLearningProgression(@PathVariable final String wordId) {
        userWordComprehensionService.resetLearningProgression(wordId);
    }
}
