package com.vocahype.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LevelOfQuiz {
    EASY("easy"),
    NORMAL("medium"),
    HARD("hard"),
    EXTREME("extreme");

    private final String detail;
    
}
