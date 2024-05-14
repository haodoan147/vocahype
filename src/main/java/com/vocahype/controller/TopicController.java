package com.vocahype.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.service.ImportService;
import com.vocahype.service.TopicService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping(Routing.TOPICS)
    ResponseEntityJsonApi getUserProfile() {
        return new ResponseEntityJsonApi(topicService.getListTopic());
    }

    @PostMapping(Routing.TOPICS)
    ResponseEntityJsonApi createTopic(@RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(topicService.createTopic(jsonNode));
    }

    @DeleteMapping(value = Routing.TOPIC_ID)
    public void deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
    }
}
