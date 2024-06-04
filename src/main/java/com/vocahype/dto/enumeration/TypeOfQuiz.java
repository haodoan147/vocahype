package com.vocahype.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeOfQuiz {
    DEFINITION("Definition"),
    SYNONYM_ANTONYM("Synonym/Antonym"),
    WORD_USAGE_IN_CONTEXT("Word usage in context"),
    WORD_USAGE_IN_SENTENCE("Word meaning in sentence");

    private final String detail;

}
