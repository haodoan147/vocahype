package com.vocahype.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vocahype.service.QuizService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping(value = Routing.WORD_QUIZ)
    public Map getQuizGen(@RequestParam String word, @RequestParam String level) throws JsonProcessingException {
        return quizService.getQuizGen(word, level);
    }
}
