package com.vocahype.service;

import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
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
    private final ModelMapper modelMapper;

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



}
