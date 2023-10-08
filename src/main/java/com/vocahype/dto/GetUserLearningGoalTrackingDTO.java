package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserLearningGoalTrackingDTO {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Timestamp dateLearn;
    private String userId;
    private Integer userLearntTime;

    @Override
    public String toString() {
        return userId + " - " + dateLearn;
    }
}
