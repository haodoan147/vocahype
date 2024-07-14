package com.vocahype.repository;

import com.vocahype.dto.FrequencyDTO;

import java.util.Collection;
import java.util.List;

public interface UserWordComprehensionRepositoryCustom {
    List<FrequencyDTO> getWordComprehension(
            String userId,
            Long topicId,
            Integer page,
            Integer size
    );

    List<FrequencyDTO> getRandomWords(
            Integer size
    );

    List<FrequencyDTO> getRandomWordsNotIn(
            Integer size,
            Collection<String> words
    );
}
