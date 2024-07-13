package com.vocahype.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public enum QuizType {
//    WORD_SCRAMBLE("word_scramble"),
    DEFINITION_SINGLE_SELECT("definition_single_select"),
    DEFINITION_MULTIPLE_SELECT("definition_multiple_select"),
    TRUE_FALSE("true_false"),
    RELATED_WORD_SELECT("related_word_select"),
    ANTONYM_SYNONYM_MATCH("antonym_synonym_match"),
//    WORD_GUESS("word_guess")
    ;

    private final String title;
    private static final Random RANDOM = new Random();

    public static QuizType  getRandomType() {
        QuizType[] values = QuizType.values();
        return values[RANDOM.nextInt(values.length)];
    }
}
