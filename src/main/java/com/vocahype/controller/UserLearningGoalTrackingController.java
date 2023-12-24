package com.vocahype.controller;

import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.dto.SaveUserLearningGoalTrackingDTO;
import com.vocahype.service.UserLearningGoalTrackingService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserLearningGoalTrackingController {
    private final UserLearningGoalTrackingService userLearningGoalTrackingService;

    @PostMapping(Routing.LEARNING_TIME)
    void saveUserLearningGoalTracking(@Valid @RequestBody SaveUserLearningGoalTrackingDTO saveUserLearningGoalTrackingDTO) {
        userLearningGoalTrackingService.saveUserLearningGoalTracking(saveUserLearningGoalTrackingDTO);
    }

    @GetMapping(Routing.LEARNING_TIME)
    ResponseEntityJsonApi getUserLearningGoalTracking() {
        return new ResponseEntityJsonApi(userLearningGoalTrackingService.getUserLearningGoalTracking());
    }
}
