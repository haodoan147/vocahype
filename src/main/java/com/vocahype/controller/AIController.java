package com.vocahype.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vocahype.dto.quiz.QuizDTO;
import com.vocahype.service.AIService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @GetMapping(value = Routing.WORD_QUIZ)
    public QuizDTO getQuizGen(@RequestParam String word) throws JsonProcessingException {
        return aiService.getQuizGen(word);
    }

    @GetMapping(Routing.WORD_STORY)
    public Map getStory(@RequestParam final long days) throws JsonProcessingException {
        return aiService.getStory(days);
    }


    @GetMapping(Routing.LIST_WORD_STORY)
    public Set<String> geListWordStory(@RequestParam final long days) {
        return aiService.getListWordStory(days);
    }
}
