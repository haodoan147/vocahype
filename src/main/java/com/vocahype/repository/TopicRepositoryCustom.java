package com.vocahype.repository;

import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.TopicDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.WordUserKnowledgeDTO;

import java.util.List;

public interface TopicRepositoryCustom {
    List<TopicDTO> getAll(String userId, Long topicId);
    List<WordDTO> getWordInTopic(Long topicId);
    List<WordUserKnowledgeDTO> getRandomWordInTopic(int size);
    List<FrequencyDTO> getFrequencyByWordIn(List<String> words);
}
