package com.vocahype.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.dto.ResponseEntityJsonApi;
import com.vocahype.service.TopicService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntityJsonApi(topicService.updateTopic(null, jsonNode));
    }

    @DeleteMapping(value = Routing.TOPIC_ID)
    public void deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
    }

    @PutMapping(value = Routing.TOPIC_ID)
    public ResponseEntityJsonApi updateTopic(@PathVariable Long topicId, @RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(topicService.updateTopic(topicId, jsonNode));
    }
}
