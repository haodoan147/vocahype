package com.vocahype.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveUserLearningGoalTrackingDTO {
    @NotNull
    private Integer time;
}
