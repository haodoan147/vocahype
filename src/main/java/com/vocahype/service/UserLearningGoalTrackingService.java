package com.vocahype.service;

import com.vocahype.dto.GetUserLearningGoalTrackingDTO;
import com.vocahype.dto.SaveUserLearningGoalTrackingDTO;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserLearningGoalTrackingRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserLearningGoalTrackingService {

    private final UserLearningGoalTrackingRepository userLearningGoalTrackingRepository;

    public void saveUserLearningGoalTracking(SaveUserLearningGoalTrackingDTO saveUserLearningGoalTrackingDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        UserLearningGoalTrackingID id = new UserLearningGoalTrackingID(LocalDate.now(), userId);
        UserLearningGoalTracking userLearningGoalTracking = userLearningGoalTrackingRepository.findById(id)
                .orElse(new UserLearningGoalTracking(id, 0));
        userLearningGoalTracking.setUserLearntTime(
                userLearningGoalTracking.getUserLearntTime() + saveUserLearningGoalTrackingDTO.getTime());
        userLearningGoalTrackingRepository.save(userLearningGoalTracking);
    }

    public GetUserLearningGoalTrackingDTO getUserLearningGoalTracking() {
        String userId = SecurityUtil.getCurrentUserId();
        UserLearningGoalTrackingID id = new UserLearningGoalTrackingID(LocalDate.now(), userId);
        UserLearningGoalTracking userLearningGoalTracking = userLearningGoalTrackingRepository.findById(id)
                .orElse(new UserLearningGoalTracking(id, 0));
        return new GetUserLearningGoalTrackingDTO(Timestamp.valueOf(
                userLearningGoalTracking.getUserLearningGoalTrackingID().getDateLearn().atStartOfDay()),
                userId,
                userLearningGoalTracking.getUserLearntTime());
    }
}
