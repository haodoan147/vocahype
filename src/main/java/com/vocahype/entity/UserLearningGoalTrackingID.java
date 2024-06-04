package com.vocahype.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UserLearningGoalTrackingID implements Serializable {
    @Column(name = "date_learn")
    private LocalDate dateLearn;
    @Column(name = "user_id")
    private String userId;
}
