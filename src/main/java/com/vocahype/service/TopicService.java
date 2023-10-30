package com.vocahype.service;

import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import com.vocahype.repository.TopicRepository;
import com.vocahype.util.GeneralUtils;
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
        return topicRepository.findAll().stream().map(GeneralUtils::convertToDto).collect(Collectors.toSet());
    }



}
