package com.vocahype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import com.vocahype.entity.WordTopic;
import com.vocahype.entity.WordTopicID;
import com.vocahype.exception.InvalidException;
import com.vocahype.exception.UserFriendlyException;
import com.vocahype.repository.TopicRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordTopicRepository;
import com.vocahype.util.GeneralUtils;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImportService importService;

    public List<TopicDTO> getListTopic() {
        String userId = SecurityUtil.getCurrentUserId();
//        Set<TopicDTO> topicDTOSet = topicRepository.findAll().stream().map(GeneralUtils::convertToDto).collect(Collectors.toSet());
//
//        topicDTOSet.forEach(topicDTO -> {
//            topicDTO.setMasteredWordCount(
//                    (long) topicRepository.countLearningWordTopicsByTopicId(topicDTO.getId(), userId, 11));
//            topicDTO.setLearningWordCount(
//                    (long) topicRepository.countLearningWordTopicsByTopicIdBetween(topicDTO.getId(), userId, 2, 11));
//        });
        return topicRepository.getAll(userId, null);
    }

    public TopicDTO getTopic(final Long topicID) {
        TopicDTO topic = topicRepository.getAll(SecurityUtil.getCurrentUserId(), topicID).stream().findFirst()
                .orElseThrow(() -> new InvalidException("Topic not found", "Topic with id " + topicID + " not found"));
        topic.setWordInTopic(new HashSet<>(topicRepository.getWordInTopic(topicID)));
        return topic;
    }

    @Transactional
    public TopicDTO updateTopic(final Long topicID, final String topic, final MultipartFile file) {
        try {
            return updateTopic(topicID, objectMapper.readTree(topic), file);
        } catch (JsonProcessingException e) {
            throw new UserFriendlyException(e.getMessage());
        }
    }

    @Transactional
    public TopicDTO updateTopic(final Long topicID, final JsonNode jsonNode, final MultipartFile file) {
        boolean isNewTopic = topicID == null;
        Topic topic = isNewTopic ? Topic.builder().wordTopics(new HashSet<>()).build()
                : topicRepository.findById(topicID)
                .orElseThrow(() -> new InvalidException("Topic not found", "Topic with id " + topicID + " not found"));
        TopicDTO resourceWord = objectMapper.convertValue(jsonNode.get("data").get(0).get("attributes"), TopicDTO.class);
        if (resourceWord.getName() != null && (isNewTopic || !topic.getName().equals(resourceWord.getName()))) {
            if (resourceWord.getName().isEmpty() || resourceWord.getName().isBlank()) {
                throw new InvalidException("Invalid request", "Topic name is required");
            }
            topicRepository.findByName(resourceWord.getName()).ifPresent(t -> {
                throw new InvalidException("Topic already exists", "Topic with name " + t.getName() + " already exists");
            });
            topic.setName(resourceWord.getName());
        }
        if (resourceWord.getDescription() != null) {
            topic.setDescription(resourceWord.getDescription());
        }
        if (resourceWord.getEmoji() != null) {
            topic.setEmoji(resourceWord.getEmoji());
        }
        if (isNewTopic) {
            topic = topicRepository.saveAndFlush(topic);
        }
        if (resourceWord.getAddedWordIds() != null || resourceWord.getRemovedWordIds() != null) {
            Topic finalTopic = topic;
            Set<WordTopic> wordList = new HashSet<>(finalTopic.getWordTopics());
            if (resourceWord.getAddedWordIds() != null) {
                if (resourceWord.getRemovedWordIds() != null && resourceWord.getAddedWordIds().stream().anyMatch(resourceWord.getRemovedWordIds()::contains)) {
                    throw new InvalidException("Invalid request", "Word cannot be added and removed at the same time");
                }
                resourceWord.getAddedWordIds().forEach(wordId -> {
                    if (wordList.stream().noneMatch(wt -> wt.getWordTopicID().getWord().equals(wordId))) {
                        wordRepository.findByWord(wordId)
                                .ifPresent(word -> wordList.add(WordTopic.builder().wordTopicID(
                                                WordTopicID.builder().topicId(finalTopic.getId()).word(word.getWord()).build())
                                        .topic(finalTopic).build()));
                    }
                });
            }
            if (resourceWord.getRemovedWordIds() != null) {
                wordList.removeIf(wt -> resourceWord.getRemovedWordIds().contains(wt.getWordTopicID().getWord()));
            }
            wordTopicRepository.saveAllAndFlush(wordList);
            wordTopicRepository.deleteAllByTopicIdAndWordNotIn(topic.getId(),
                    wordList.stream().map(wt -> wt.getWordTopicID().getWord()).collect(Collectors.toSet()));
            topic.setWordTopics(wordList);
        }
        if (file != null) {
            importService.importWordInTopic(file, topic.getId().intValue());
        }
        return GeneralUtils.convertToDto(topic);
    }

    public void deleteTopic(final Long topicId) {
        topicRepository.findById(topicId).ifPresent(topicRepository::delete);
    }
}
