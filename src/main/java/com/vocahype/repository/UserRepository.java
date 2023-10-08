package com.vocahype.repository;

import com.vocahype.entity.User;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
