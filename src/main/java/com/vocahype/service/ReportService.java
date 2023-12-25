package com.vocahype.service;

import com.vocahype.dto.ReportDTO;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import com.vocahype.repository.UserLearningGoalTrackingRepository;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.util.GeneralUtils;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserLearningGoalTrackingRepository userLearningGoalTrackingRepository;
    private final UserWordComprehensionRepository userWordComprehensionRepository;

    public ReportDTO getReport(final Long dateStart, final Long dateEnd) {
        String userId = SecurityUtil.getCurrentUserId();
        LocalDate localDateStart = new Timestamp(dateStart).toLocalDateTime().toLocalDate();
        LocalDate localDateEnd = new Timestamp(dateEnd).toLocalDateTime().toLocalDate();
        Map<String, Integer> localDateMap = GeneralUtils.generateDateMap(localDateStart, localDateEnd);
        userLearningGoalTrackingRepository.findByUserLearningGoalTrackingID_UserIdAndUserLearningGoalTrackingID_DateLearnBetween(
                userId,
                localDateStart,
                localDateEnd).forEach(userLearningGoalTracking -> localDateMap.put(
                        userLearningGoalTracking.getUserLearningGoalTrackingID().getDateLearn().toString(),
                        userLearningGoalTracking.getUserLearntTime()));
        long mastered = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(11));
        long learning = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(2, 3, 4, 5, 6, 7, 8, 9, 10));
        long ignored = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(12));
        return new ReportDTO(localDateMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList()), mastered, learning, ignored);
    }
}
