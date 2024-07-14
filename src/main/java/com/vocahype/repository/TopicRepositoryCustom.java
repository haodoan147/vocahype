package com.vocahype.repository;

import com.vocahype.dto.TopicDTO;
import com.vocahype.dto.WordDTO;

import java.util.List;

public interface TopicRepositoryCustom {
    List<TopicDTO> getAll(String userId, Long topicId);
    List<WordDTO> getWordInTopic(Long topicId);

}
