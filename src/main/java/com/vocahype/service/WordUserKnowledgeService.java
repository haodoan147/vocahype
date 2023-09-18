package com.vocahype.service;

import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.entity.User;
import com.vocahype.entity.Word;
import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordUserKnowledgeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.vocahype.util.Constants.CURRENT_USER_ID;
import static com.vocahype.util.Constants.WORD_COUNT;

@Service
@RequiredArgsConstructor
public class WordUserKnowledgeService {

    public static final int BIAS = 10;
    private final WordRepository wordRepository;
    private final WordUserKnowledgeRepository wordUserKnowledgeRepository;
    private final ModelMapper modelMapper;

    public List<WordUserKnowledgeDTO> get50WordForUserKnowledge() {
        List<Long> randomIds = new ArrayList<>();
        LongRange[] ranges = {
                new LongRange(1L, WORD_COUNT / BIAS, 10),
                new LongRange(WORD_COUNT / BIAS + 1 , WORD_COUNT / BIAS * 2, 9),
                new LongRange(WORD_COUNT / BIAS * 2 + 1, WORD_COUNT / BIAS * 3, 7),
                new LongRange(WORD_COUNT / BIAS * 3 + 1, WORD_COUNT / BIAS * 4, 6),
                new LongRange(WORD_COUNT / BIAS * 4 + 1, WORD_COUNT / BIAS * 5, 5),
                new LongRange(WORD_COUNT / BIAS * 5 + 1, WORD_COUNT / BIAS * 6, 4),
                new LongRange(WORD_COUNT / BIAS * 6 + 1, WORD_COUNT / BIAS * 7, 3),
                new LongRange(WORD_COUNT / BIAS * 7 + 1, WORD_COUNT / BIAS * 8, 3),
                new LongRange(WORD_COUNT / BIAS * 8 + 1, WORD_COUNT / BIAS * 9, 2),
                new LongRange(WORD_COUNT / BIAS * 9 + 1, WORD_COUNT, 1)
        };
        for (LongRange range : ranges) {
            randomIds.addAll(getRandomNumbersInRange(range.getMinId(), range.getMaxId(), range.getNumRecords()));
        }
        return wordRepository.findAllById(randomIds).stream()
                .map(word -> modelMapper.map(word, WordUserKnowledgeDTO.class))
                .collect(Collectors.toList());
    }

    private static Set<Long> getRandomNumbersInRange(long min, long max, int count) {
        if (count > (max - min + 1)) {
            throw new IllegalArgumentException("Count must not exceed the range between min and max.");
        }
        Set<Long> randomNumbers = new HashSet<>();
        while (randomNumbers.size() < count) {
            randomNumbers.add(ThreadLocalRandom.current().nextLong(min, max + 1));
        }
        return randomNumbers;
    }

    public void saveUserKnowledge(final List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        Set<WordUserKnowledge> knownWords = new HashSet<>();
        wordUserKnowledgeDTO.forEach(word -> {
            if (word.getStatus()) {
                knownWords.add(new WordUserKnowledge(
                        new WordUserKnowledgeID(word.getWordId(), CURRENT_USER_ID),
                        true,
                        Word.builder().id(word.getWordId()).build(),
                        User.builder().id(CURRENT_USER_ID).build())
                );
            }
        });
        wordUserKnowledgeRepository.saveAll(knownWords);
    }

    @Transactional
    public void resetUserKnowledge() {
        wordUserKnowledgeRepository.deleteAllByWordUserKnowledgeID_UserId(CURRENT_USER_ID);
    }

    public List<WordUserKnowledgeDTO> getListWordUserKnowledge() {
        long currentUserId = CURRENT_USER_ID;
        return wordUserKnowledgeRepository.findAllByWordUserKnowledgeID_UserId(currentUserId);
    }

    @AllArgsConstructor
    @Getter
    private static class LongRange {
        private long minId;
        private long maxId;
        private int numRecords;
    }
}
