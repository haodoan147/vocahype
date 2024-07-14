package com.vocahype.service;

import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.entity.User;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.*;
import com.vocahype.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordUserKnowledgeService {

    public static final int BIAS = 3;
    public static final long WORD_DATA_COUNT = 1000;
    private final WordRepository wordRepository;
    private final WordUserKnowledgeRepository wordUserKnowledgeRepository;
    private final UserRepository userRepository;
    private final UserWordComprehensionRepository userWordComprehensionRepository;
    private final TopicRepository topicRepository;

    public List<WordUserKnowledgeDTO> get50WordForUserKnowledge() {
        return topicRepository.getRandomWordInTopic(50);
    }

    public static int getTotalFrequency(List<FrequencyDTO> frequencyList) {
        int totalFrequency = 0;
        for (FrequencyDTO dto : frequencyList) {
            if (dto.getFrequency() != null) {
                totalFrequency += dto.getFrequency();
            }
        }
        return totalFrequency;
    }

    public Map<String, Object> saveUserKnowledge(final List<WordUserKnowledgeDTO> wordUserKnowledgeDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidException("User not found", "Not found any user with id: " + userId));
        Set<UserWordComprehension> knownWords = new HashSet<>();
        List<FrequencyDTO> frequencyByWordIn = topicRepository.getFrequencyByWordIn(wordUserKnowledgeDTO.stream().map(WordUserKnowledgeDTO::getWord).collect(Collectors.toList()));
        AtomicReference<Integer> sumKnown = new AtomicReference<>(0);
        wordUserKnowledgeDTO.forEach(word -> {
            Optional<FrequencyDTO> wordOptional = frequencyByWordIn.stream().filter(word1 -> word1.getWord().equals(word.getWord())).findFirst();
            if (wordOptional.isEmpty()) {
                return;
            }
            if (word.getStatus()) {
                sumKnown.updateAndGet(v -> v + wordOptional.get().getFrequency());
                knownWords.add(UserWordComprehension.builder().userWordComprehensionID(UserWordComprehensionID.builder()
                        .userId(userId).word(word.getWord()).build())
                        .user(User.builder().id(userId).build()).wordComprehensionLevel(Level.LEVEL_11.getLevel()).build());
            } else {
                knownWords.add(UserWordComprehension.builder().userWordComprehensionID(UserWordComprehensionID.builder()
                        .userId(userId).word(word.getWord()).build()).wordComprehensionLevel(Level.LEVEL_1.getLevel())
                                .user(User.builder().id(userId).build())
                        .nextLearning(Timestamp.valueOf(LocalDateTime.now()
                                .plusDays(Level.LEVEL_1.getDay())
                                .truncatedTo(ChronoUnit.DAYS))).build());
            }
        });
        userWordComprehensionRepository.saveAll(knownWords);
        double score = (double) sumKnown.get() / getTotalFrequency(frequencyByWordIn) * 1000;
        user.setScore((int) score);
        userRepository.save(user);
        return Map.of("message", "We estimate your knowledge is " + score
                + " words. Congratulation on the good work!", "estimate", score);
    }

    @Transactional
    public void resetUserKnowledge() {
        userWordComprehensionRepository.deleteAllByUserWordComprehensionID_UserId(SecurityUtil.getCurrentUserId());
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
