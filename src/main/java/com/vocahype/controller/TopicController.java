package com.vocahype.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.vocahype.dto.ResponseEntityJsonApi;
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
    ResponseEntityJsonApi getUserProfiles() {
        return new ResponseEntityJsonApi(topicService.getListTopic());
    }

    @GetMapping(Routing.TOPIC_ID)
    ResponseEntityJsonApi getUserProfile(@PathVariable Long topicId) {
        return new ResponseEntityJsonApi(topicService.getTopic(topicId));
    }

    @PostMapping(Routing.TOPICS)
    ResponseEntityJsonApi createTopic(@RequestParam("topic") String topic,
                                      @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseEntityJsonApi(topicService.updateTopic(null, topic, file));
    }

    @DeleteMapping(value = Routing.TOPIC_ID)
    public void deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
    }

    @PutMapping(value = Routing.TOPIC_ID)
    public ResponseEntityJsonApi updateTopic(@PathVariable Long topicId, @RequestBody JsonNode jsonNode) {
        return new ResponseEntityJsonApi(topicService.updateTopic(topicId, jsonNode, null));
    }
}
