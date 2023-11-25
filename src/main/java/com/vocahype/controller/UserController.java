package com.vocahype.controller;


import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.UserProfileDTO;
import com.vocahype.service.SynonymService;
import com.vocahype.service.UserService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(Routing.DAILY_GOAL)
    void saveUserLearningGoalTracking(@RequestBody UserProfileDTO userProfileDTO) {
        userService.saveUserLearningGoal(userProfileDTO);
    }

    @GetMapping(Routing.PROFILE)
    ResponseEntityJsonApi getUserProfile() {
        return new ResponseEntityJsonApi(userService.getUserProfile());
    }

    @PostMapping(Routing.PROFILE_TOPIC)
    void saveUserTopic(@PathVariable Long topicId) {
        userService.saveUserTopic(topicId);
    }
}
