package com.vocahype.util;

import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class GeneralUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static TopicDTO convertToDto(Topic topic) {
        String userId = SecurityUtil.getCurrentUserId();
        TopicDTO topicDTO = modelMapper.map(topic, TopicDTO.class);
        topicDTO.setWordCount((long) topic.getWordTopics().size());
        return topicDTO;
    }
}
