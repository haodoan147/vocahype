package com.vocahype.util;

import com.vocahype.dto.TopicDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GeneralUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static TopicDTO convertToDto(Topic topic) {
        return convertToDto(topic, false);
    }

    public static TopicDTO convertToDto(Topic topic, boolean includeWordTopics) {
        TopicDTO topicDTO = modelMapper.map(topic, TopicDTO.class);
        if (topic.getWordTopics() != null) {
            topicDTO.setWordCount((long) topic.getWordTopics().size());
            if (includeWordTopics) {
                topicDTO.setWordInTopic(topic.getWordTopics().stream().map(wordTopic -> WordDTO.builder().id(wordTopic.getWord().getId())
                        .word(wordTopic.getWord().getWord()).build()).collect(Collectors.toSet()));
            }
        }
        return topicDTO;
    }

    public static Map<String, Integer> generateDateMap(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> dateMap = new HashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dateMap.put(currentDate.toString(), 0);
            currentDate = currentDate.plusDays(1);
        }
        return dateMap;
    }
}
