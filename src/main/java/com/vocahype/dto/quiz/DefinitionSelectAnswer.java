package com.vocahype.dto.quiz;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DefinitionSelectAnswer {
    private String text;
    private boolean correct;
}
