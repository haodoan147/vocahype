package com.vocahype.service;

import com.vocahype.dto.UserProfileDTO;
import com.vocahype.dto.enumeration.LearningGoal;
import com.vocahype.entity.Topic;
import com.vocahype.entity.User;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.TopicRepository;
import com.vocahype.repository.UserRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public void saveUserLearningGoal(UserProfileDTO userProfileDTO) {
        try {
            String userId = SecurityUtil.getCurrentUserId();
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
        String userId = SecurityUtil.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User not found"));
    }

    public void saveUserTopic(final Long topicId) {
        String userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User id: " + userId));
        if (user.getTopic() == null || !user.getTopic().getId().equals(topicId)) {
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new InvalidException("Topic not found", "Topic id: " + topicId));
            user.setTopic(topic);
            userRepository.save(user);
        }
    }
}
