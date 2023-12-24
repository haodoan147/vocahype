package com.vocahype.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.request.QuizRequest;
import com.vocahype.service.QuizService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping(value = Routing.WORD_QUIZ)
    public ResponseEntityJsonApi getQuizGen(@Valid @RequestBody QuizRequest quizRequest) throws JsonProcessingException {
        return new ResponseEntityJsonApi(quizService.getQuizGen(quizRequest));
    }
}
