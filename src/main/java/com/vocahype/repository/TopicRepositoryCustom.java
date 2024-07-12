package com.vocahype.repository;

import com.vocahype.dto.TopicDTO;

import java.util.List;

public interface TopicRepositoryCustom {
    List<TopicDTO> getAll(String userId, Long topicId);

}
