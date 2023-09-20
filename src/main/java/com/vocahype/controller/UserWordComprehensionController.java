package com.vocahype.controller;

import com.vocahype.dto.MetaResponseEntity;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.exception.InvalidException;
import com.vocahype.service.UserWordComprehensionService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = Routing.WORDS_LEARN)
    public ResponseEntityJsonApi getWordTest(@RequestParam(name = "page[offset]") int offset,
                                             @RequestParam(name = "page[limit]") int limit) {
        if (offset <= 0 || limit <= 0) {
            throw new InvalidException("Invalid param", "Offset and limit must be greater than 0!");
        }
        List<WordDTO> wordTest = userWordComprehensionService.getWordTest(offset - 1, limit);
        long total = userWordComprehensionService.countWordTest();
        return new ResponseEntityJsonApi(wordTest, new MetaResponseEntity(1, (int) Math.ceil(total / (double)limit), offset, limit, (int) total));
    }
}
