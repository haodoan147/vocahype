package com.vocahype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.configuration.FirebaseAuthenticationFilter;
import com.vocahype.dto.UserProfileDTO;
import com.vocahype.dto.UserTopicDTO;
import com.vocahype.dto.enumeration.LearningGoal;
import com.vocahype.dto.enumeration.RoleTitle;
import com.vocahype.entity.Role;
import com.vocahype.entity.Topic;
import com.vocahype.entity.User;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    public void saveUserLearningGoal(UserProfileDTO userProfileDTO) {
        try {
            String userId = SecurityUtil.getCurrentUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new InvalidException("User not found", "User id: " + userId + " not found"));
            LearningGoal learningGoal = LearningGoal.valueOf(userProfileDTO.getLevel().toLowerCase());
            user.setGoalSeconds(learningGoal.getSeconds());
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Invalid learning goal",
                    userProfileDTO.getLevel().toLowerCase() + " learning goal is not valid");
        }
    }

    public User getUserProfile() {
        String userId = SecurityUtil.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User not found"));
    }

    public void saveUserTopic(final UserTopicDTO userTopicDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User id: " + userId));
        if (userTopicDTO.getTopicId() == null || !userTopicDTO.getTopicId().isPresent()){
            throw new InvalidException("Topic id is required", "Topic id is required in request body");
        }
        if (userTopicDTO.getTopicId().get() != null) {
            if (user.getTopic() == null || !user.getTopic().getId().equals(userTopicDTO.getTopicId().get())) {
                Topic topic = topicRepository.findById(userTopicDTO.getTopicId().get())
                        .orElseThrow(() -> new InvalidException("Topic not found", "Topic id: "
                                + userTopicDTO.getTopicId().get()));
                user.setTopic(topic);
                userRepository.save(user);
            }
        } else if (user.getTopic() != null) {
            user.setTopic(null);
            userRepository.save(user);
        }
    }

    public User saveUser(final JsonNode jsonNode, final String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found", "User id: " + userId));
        jsonNode.get("included").forEach(included -> {
            if (included.get("type").asText().equals("role")) {
                long roleId = included.get("id").asLong();
                if (user.getRole().getId().equals(roleId)) {
                    return;
                }
                String currentUserRole = SecurityUtil.getCurrentUserRole();
                if (currentUserRole.equals(RoleTitle.CONTRIBUTOR.getTitle()) && user.getRole().getId() > 1) {
                    throw new InvalidException("Unauthorized", "Only admin can change contributor and admin role");
                }
                Role role = roleRepository.findById(roleId).orElseThrow(() -> new InvalidException("Role not found", "Role id: " + roleId));
                user.setRole(role);
                firebaseAuthenticationFilter.clearCache(userId);
            }
        });
        return userRepository.save(user);
    }
}
