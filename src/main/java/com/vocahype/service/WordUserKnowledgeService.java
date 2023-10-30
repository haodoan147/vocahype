package com.vocahype.service;

import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.entity.User;
import com.vocahype.entity.Word;
import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordUserKnowledgeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
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
    private final UserWordComprehensionService userWordComprehensionService;

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

    public Map<String, Object> saveUserKnowledge(final List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        Set<WordUserKnowledge> knownWords = new HashSet<>();
        AtomicReference<Double> score = new AtomicReference<>((double) 0);
        wordUserKnowledgeDTO.forEach(word -> {
            Word word1 = wordRepository.findById(word.getWordId()).orElseThrow(() -> new InvalidException("Word not found", "wordId: " + word.getWordId().toString()));
            if (word.getStatus()) {
                knownWords.add(new WordUserKnowledge(
                        new WordUserKnowledgeID(word.getWordId(), CURRENT_USER_ID),
                        true,
                        Word.builder().id(word.getWordId()).build(),
                        User.builder().id(CURRENT_USER_ID).build())
                );
                score.updateAndGet(v -> v + (1 - (1 - word1.getPoint())));
                userWordComprehensionService.saveWordUserKnowledge(word.getWordId(), Assessment.MASTERED);
            } else {
                score.updateAndGet(v -> v - (1 - word1.getPoint()));
                userWordComprehensionService.saveWordUserKnowledge(word.getWordId(), Assessment.HARD);
            }
        });
        wordUserKnowledgeRepository.saveAll(knownWords);
        double sum = score.get() <= 0 ? 0 : (score.get() / wordUserKnowledgeDTO.size() * WORD_COUNT);
        int formatted = (int) sum;
        return Map.of("message", "We estimate your knowledge is " + formatted
                + " words. Congratulation on the good work!", "estimate", formatted);
    }

    @Transactional
    public void resetUserKnowledge() {
        wordUserKnowledgeRepository.deleteAllByWordUserKnowledgeID_UserId(CURRENT_USER_ID);
    }

    public List<WordUserKnowledgeDTO> getListWordUserKnowledge() {
        String currentUserId = CURRENT_USER_ID;
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
