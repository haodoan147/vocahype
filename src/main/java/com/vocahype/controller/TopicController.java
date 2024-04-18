package com.vocahype.controller;


import com.vocahype.service.ImportService;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TopicController {
    private final ImportService importService;

    @PostMapping(Routing.IMPORT)
    void getUserProfile(@RequestParam("file") MultipartFile file,
                        @RequestParam("topicId") Integer topicId) {
        importService.importWordInTopic(file, topicId);
    }

}
