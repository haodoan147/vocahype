package com.vocahype.service;

import com.vocahype.configuration.ApplicationProperties;
import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.entity.*;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordUserKnowledgeRepository;
import com.vocahype.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.vocahype.util.Constants.WORD_COUNT;

@Service
@RequiredArgsConstructor
public class WordUserKnowledgeService {

    public static final int BIAS = 3;
    private final WordRepository wordRepository;
    private final WordUserKnowledgeRepository wordUserKnowledgeRepository;
    private final ModelMapper modelMapper;
    private final UserWordComprehensionService userWordComprehensionService;
    private final UserRepository userRepository;
    private final ApplicationProperties applicationProperties;

    public List<WordUserKnowledgeDTO> get50WordForUserKnowledge() {
        long wordDataCount = 3000;
        if (wordDataCount < 50) {
            throw new InvalidException("Word data count is not enough", "wordDataCount: " + wordDataCount);
        }
        List<Long> randomIds = new ArrayList<>();
        LongRange[] ranges = {
                new LongRange(1L, wordDataCount / BIAS, 20),
                new LongRange(wordDataCount / BIAS + 1 , wordDataCount / BIAS * 2, 15),
                new LongRange(wordDataCount / BIAS * 2 + 1, wordDataCount, 15)
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
        String userId = SecurityUtil.getCurrentUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseGet(() -> userRepository.save(
                User.builder().id(userId).loginName(userId).firstName(userId).lastName("").status(1L).loginCount(0L)
                        .createdOn(Timestamp.valueOf(LocalDateTime.now())).role(Role.builder().id(1L).build()).build()));
        Set<WordUserKnowledge> knownWords = new HashSet<>();
        AtomicReference<Double> score = new AtomicReference<>((double) 0);
        wordUserKnowledgeDTO.forEach(word -> {
            Word word1 = wordRepository.findById(word.getWordId()).orElseThrow(() -> new InvalidException("Word not found", "wordId: " + word.getWordId().toString()));
            if (word.getStatus()) {
                knownWords.add(new WordUserKnowledge(
                        new WordUserKnowledgeID(word.getWordId(), userId),
                        true,
                        Word.builder().id(word.getWordId()).build(),
                        user)
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
        user.setScore(formatted);
        userRepository.save(user);
        return Map.of("message", "We estimate your knowledge is " + formatted
                + " words. Congratulation on the good work!", "estimate", formatted);
    }

    @Transactional
    public void resetUserKnowledge() {
        wordUserKnowledgeRepository.deleteAllByWordUserKnowledgeID_UserId(SecurityUtil.getCurrentUserId());
    }

    public List<WordUserKnowledgeDTO> getListWordUserKnowledge() {
        String currentUserId = SecurityUtil.getCurrentUserId();
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
