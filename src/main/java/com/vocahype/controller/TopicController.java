package com.vocahype.controller;


import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.service.TopicService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping(Routing.TOPICS)
    ResponseEntityJsonApi getUserProfile() {
        return new ResponseEntityJsonApi(topicService.getListTopic());
    }

}
