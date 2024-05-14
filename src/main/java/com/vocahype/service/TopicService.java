package com.vocahype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import com.vocahype.entity.Word;
import com.vocahype.entity.WordTopic;
import com.vocahype.entity.WordTopicID;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.TopicRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordTopicRepository;
import com.vocahype.util.GeneralUtils;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final ObjectMapper objectMapper;
    private final WordRepository wordRepository;
    private final WordTopicRepository wordTopicRepository;

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

    @Transactional
    public TopicDTO createTopic(final JsonNode jsonNode) {
        Topic topic = Topic.builder().build();
        TopicDTO resourceWord = objectMapper.convertValue(jsonNode.get("data").get(0).get("attributes"), TopicDTO.class);
        topicRepository.findByName(resourceWord.getName()).ifPresent(t -> {
            throw new InvalidException("Topic already exists", "Topic with name " + t.getName() + " already exists");
        });
        topic.setName(resourceWord.getName());
        topic.setDescription(resourceWord.getDescription());
        topic.setEmoji(resourceWord.getEmoji());
        topic = topicRepository.saveAndFlush(topic);
        if (resourceWord.getWordList() != null && !resourceWord.getWordList().isEmpty()) {
            Set<WordTopic> wordList = new HashSet<>();
            Topic finalTopic = topic;
            resourceWord.getWordList().forEach(wordId -> wordRepository.findById(wordId)
                    .ifPresent(word -> wordList.add(WordTopic.builder().wordTopicID(
                            WordTopicID.builder().topicId(finalTopic.getId()).wordId(word.getId()).build())
                            .word(word).topic(finalTopic).build())));
            topic.setWordTopics(wordList);
            wordTopicRepository.saveAllAndFlush(wordList);
            topic = topicRepository.save(topic);
        }
        return GeneralUtils.convertToDto(topic);
    }

    public void deleteTopic(final Long topicId) {
        topicRepository.findById(topicId).ifPresent(topicRepository::delete);
    }
}
