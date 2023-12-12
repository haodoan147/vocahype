package com.vocahype.controller;


import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.UserProfileDTO;
import com.vocahype.dto.UserTopicDTO;
import com.vocahype.service.UserService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(Routing.DAILY_GOAL)
    void saveUserLearningGoalTracking(@Valid @RequestBody UserProfileDTO userProfileDTO) {
        userService.saveUserLearningGoal(userProfileDTO);
    }

    @GetMapping(Routing.PROFILE)
    ResponseEntityJsonApi getUserProfile() {
        return new ResponseEntityJsonApi(userService.getUserProfile());
    }

    @PostMapping(Routing.PROFILE_TOPIC)
    void saveUserTopic(@Valid @RequestBody UserTopicDTO userTopicDTO) {
        userService.saveUserTopic(userTopicDTO);
    }
}
