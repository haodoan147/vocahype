package com.vocahype.service;

import com.vocahype.dto.UserProfileDTO;
import com.vocahype.dto.enumeration.LearningGoal;
import com.vocahype.entity.User;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String CURRENT_USER_ID = "100000";
    private final UserRepository userRepository;

    public void saveUserLearningGoal(UserProfileDTO userProfileDTO) {
        try {
            String userId = CURRENT_USER_ID;
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new InvalidException("User not found", "User not found"));
            LearningGoal learningGoal = LearningGoal.valueOf(userProfileDTO.getLevel().toLowerCase());
            user.setGoalSeconds(learningGoal.getSeconds());
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Invalid learning goal", "Invalid learning goal");
        }
    }

    public User getUserProfile() {
        String userId = CURRENT_USER_ID;
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User not found"));
    }

}
