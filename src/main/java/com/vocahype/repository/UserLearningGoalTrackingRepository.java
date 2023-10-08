package com.vocahype.repository;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserLearningGoalTrackingRepository extends JpaRepository<UserLearningGoalTracking, UserLearningGoalTrackingID> {

}
