package com.vocahype.service;

import com.vocahype.dto.ReportDTO;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.repository.UserLearningGoalTrackingRepository;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserLearningGoalTrackingRepository userLearningGoalTrackingRepository;
    private final UserWordComprehensionRepository userWordComprehensionRepository;

    public ReportDTO getReport(final Long dateStart, final Long dateEnd) {
        String userId = SecurityUtil.getCurrentUserId();
        List<UserLearningGoalTracking> goalTrackings = userLearningGoalTrackingRepository.findByUserLearningGoalTrackingID_UserIdAndUserLearningGoalTrackingID_DateLearnBetween(
                userId,
                new Timestamp(dateStart).toLocalDateTime().toLocalDate(),
                new Timestamp(dateEnd).toLocalDateTime().toLocalDate());
        long mastered = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(11));
        long learning = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(2, 3, 4, 5, 6, 7, 8, 9, 10));
        long ignored = userWordComprehensionRepository.countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(
                userId,
                List.of(12));
        return new ReportDTO(goalTrackings, mastered, learning, ignored);
    }
}
