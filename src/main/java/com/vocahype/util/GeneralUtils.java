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
        topic.getWordTopics().forEach(wordTopic -> {
            topicDTO.setLearningWordCount(wordTopic.getWord().getUserWordComprehensions().stream()
                    .filter(userWordComprehension -> userWordComprehension.getWordComprehensionLevel() == 11
                            && userWordComprehension.getUserWordComprehensionID().getUserId().equals(userId)).count());
            topicDTO.setMasteredWordCount(wordTopic.getWord().getUserWordComprehensions().stream()
                    .filter(userWordComprehension -> userWordComprehension.getWordComprehensionLevel() > 2
                            && userWordComprehension.getWordComprehensionLevel() < 11
                            && userWordComprehension.getUserWordComprehensionID().getUserId().equals(userId)).count());
        });
        return topicDTO;
    }
}
