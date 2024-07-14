package com.vocahype.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
@AllArgsConstructor
public enum QuizType {
    WORD_SCRAMBLE("word_scramble"),
    DEFINITION_SINGLE_SELECT("definition_single_select"),
    DEFINITION_MULTIPLE_SELECT("definition_multiple_select"),
    TRUE_FALSE("true_false"),
    RELATED_WORD_SELECT("related_word_select"),
    ANTONYM_SYNONYM_MATCH("antonym_synonym_match"),
    WORD_GUESS("word_guess")
    ;

    private final String title;
    private static final Random RANDOM = new Random();

    public static QuizType getRandomTypeSingle() {
        QuizType[] values = QuizType.values();
        List<Integer> indexes = List.of(1, 2, 3, 4);
        return values[indexes.get(RANDOM.nextInt(indexes.size()))];
    }

    public static QuizType getRandomTypeMulti() {
        QuizType[] values = QuizType.values();
        List<Integer> indexes = List.of(0, 1, 2, 3, 4, 6);
        return values[indexes.get(RANDOM.nextInt(indexes.size()))];
    }
}
