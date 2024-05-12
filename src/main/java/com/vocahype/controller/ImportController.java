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
public class ImportController {
    private final ImportService importService;

    @PostMapping(Routing.IMPORT)
    void getUserProfile(@RequestParam("file") MultipartFile file,
                        @RequestParam("topicId") Integer topicId) {
        importService.importWordInTopic(file, topicId);
    }

}
