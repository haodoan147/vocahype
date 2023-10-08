package com.vocahype.service;

import com.vocahype.dto.GetUserLearningGoalTrackingDTO;
import com.vocahype.dto.SaveUserLearningGoalTrackingDTO;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserLearningGoalTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserLearningGoalTrackingService {

    private static final String CURRENT_USER_ID = "100000";
    private final UserLearningGoalTrackingRepository userLearningGoalTrackingRepository;

    public void saveUserLearningGoalTracking(SaveUserLearningGoalTrackingDTO saveUserLearningGoalTrackingDTO) {
        String userId = CURRENT_USER_ID;
        UserLearningGoalTrackingID id = new UserLearningGoalTrackingID(LocalDate.now(), userId);
        UserLearningGoalTracking userLearningGoalTracking = userLearningGoalTrackingRepository.findById(id)
                .orElse(new UserLearningGoalTracking(id, 0));
        userLearningGoalTracking.setUserLearntTime(
                userLearningGoalTracking.getUserLearntTime() + saveUserLearningGoalTrackingDTO.getTime());
        userLearningGoalTrackingRepository.save(userLearningGoalTracking);
    }

    public GetUserLearningGoalTrackingDTO getUserLearningGoalTracking() {
        String userId = CURRENT_USER_ID;
        UserLearningGoalTrackingID id = new UserLearningGoalTrackingID(LocalDate.now(), userId);
        UserLearningGoalTracking userLearningGoalTracking = userLearningGoalTrackingRepository.findById(id)
                .orElseThrow(() ->
                        new InvalidException("User have not learnt today!", "User have not learnt yet today!"));
        return new GetUserLearningGoalTrackingDTO(Timestamp.valueOf(
                userLearningGoalTracking.getUserLearningGoalTrackingID().getDateLearn().atStartOfDay()),
                userLearningGoalTracking.getUserLearningGoalTrackingID().getUserId(),
                userLearningGoalTracking.getUserLearntTime());
    }
}
