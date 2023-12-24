package com.vocahype.dto;

import com.vocahype.dto.enumeration.LevelOfQuiz;
import com.vocahype.dto.enumeration.TypeOfQuiz;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
@Getter
public class SingleSelectQuiz {
    TypeOfQuiz typeOfQuiz;
    LevelOfQuiz levelOfQuiz;
    String word;
    String question;
}
