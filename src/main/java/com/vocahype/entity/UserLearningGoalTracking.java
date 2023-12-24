package com.vocahype.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "user_learning_goal_tracking", schema = "learning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningGoalTracking implements Serializable {

    @EmbeddedId
    private UserLearningGoalTrackingID userLearningGoalTrackingID;

    @Column(name = "user_learnt_time")
    private Integer userLearntTime;

//    @MapsId("dateLearn")
//    @Column(name = "date_learn")
//    private Date dateLearn;
//
//    @MapsId("userId")
//    @Column(name = "user_id")
//    private String userId;

    @Override
    public String toString() {
        return " - ";
    }
}
