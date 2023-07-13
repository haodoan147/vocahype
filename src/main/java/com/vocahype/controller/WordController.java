package com.vocahype.controller;

import com.vocahype.entity.Word;
import com.vocahype.service.WordService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping(value = Routing.WORD)
    public List<Word> getMeterIntegrity() {
        return wordService.getWordList();
    }
}
