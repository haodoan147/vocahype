package com.vocahype.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vocahype.service.AIService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @GetMapping(value = Routing.WORD_QUIZ)
    public Map getQuizGen(@RequestParam String word, @RequestParam String level) throws JsonProcessingException {
        return aiService.getQuizGen(word, level);
    }

    @GetMapping(Routing.WORD_STORY)
    public Map getStory(@RequestParam long days) throws JsonProcessingException {
        return aiService.getStory(days);
    }
}
