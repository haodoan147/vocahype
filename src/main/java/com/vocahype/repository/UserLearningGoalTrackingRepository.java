package com.vocahype.repository;

import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserLearningGoalTrackingRepository extends JpaRepository<UserLearningGoalTracking, UserLearningGoalTrackingID> {

    List<UserLearningGoalTracking> findByUserLearningGoalTrackingID_UserIdAndUserLearningGoalTrackingID_DateLearnBetween(String userLearningGoalTrackingID_userId, LocalDate dateStart, LocalDate dateEnd);
}
