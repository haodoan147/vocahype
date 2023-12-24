package com.vocahype.dto;

import com.vocahype.entity.UserLearningGoalTracking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private List<String> labels;
    private List<Long> datas;
    private Long mastered;
    private Long learning;
    private Long ignored;

    public ReportDTO(List<UserLearningGoalTracking> userLearningGoalTrackings, Long mastered, Long learning, Long ignored) {
        this.labels = new ArrayList<>();
        this.datas = new ArrayList<>();
        userLearningGoalTrackings.forEach(userLearningGoalTracking -> {
            labels.add(userLearningGoalTracking.getUserLearningGoalTrackingID().getDateLearn().toString());
            datas.add(userLearningGoalTracking.getUserLearntTime().longValue());
        });
        this.mastered = mastered;
        this.learning = learning;
        this.ignored = ignored;
    }

    @Override
    public String toString() {
        return labels.toString();
    }
}
