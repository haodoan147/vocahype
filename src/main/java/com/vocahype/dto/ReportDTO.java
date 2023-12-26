package com.vocahype.dto;

import com.vocahype.entity.UserLearningGoalTracking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private List<String> labels;
    private List<Integer> datas;
    private Long mastered;
    private Long learning;
    private Long ignored;

    public ReportDTO(List<Map.Entry<String, Integer>> localDateMap, Long mastered, Long learning, Long ignored) {
        this.labels = new ArrayList<>();
        this.datas = new ArrayList<>();
        localDateMap.forEach(stringIntegerEntry -> {
            this.labels.add(stringIntegerEntry.getKey());
            this.datas.add(stringIntegerEntry.getValue());

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
