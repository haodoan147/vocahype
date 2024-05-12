package com.vocahype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.TopicRepository;
import com.vocahype.util.GeneralUtils;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final ObjectMapper objectMapper;

    public Set<TopicDTO> getListTopic() {
        String userId = SecurityUtil.getCurrentUserId();
        Set<TopicDTO> topicDTOSet = topicRepository.findAll().stream().map(GeneralUtils::convertToDto).collect(Collectors.toSet());

        topicDTOSet.forEach(topicDTO -> {
            topicDTO.setMasteredWordCount(
                    (long) topicRepository.countLearningWordTopicsByTopicId(topicDTO.getId(), userId, 11));
            topicDTO.setLearningWordCount(
                    (long) topicRepository.countLearningWordTopicsByTopicIdBetween(topicDTO.getId(), userId, 2, 11));
        });
        return topicDTOSet;
    }

    public TopicDTO createTopic(final JsonNode jsonNode) {
        Topic topic = Topic.builder().build();
        TopicDTO resourceWord = objectMapper.convertValue(jsonNode.get("data").get(0).get("attributes"), TopicDTO.class);
        topicRepository.findByName(resourceWord.getName()).ifPresent(t -> {
            throw new InvalidException("Topic already exists", "Topic with name " + t.getName() + " already exists");
        });
        topic.setName(resourceWord.getName());
        topic.setDescription(resourceWord.getDescription());
        topic.setEmoji(resourceWord.getEmoji());
        return GeneralUtils.convertToDto(topicRepository.save(topic));
    }
}
