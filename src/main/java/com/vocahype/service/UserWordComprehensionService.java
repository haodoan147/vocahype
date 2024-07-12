package com.vocahype.service;

import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.FrequencyResponseDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.entity.User;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserRepository;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.repository.WordRepository;
import com.vocahype.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWordComprehensionService {

    private final UserWordComprehensionRepository userWordComprehensionRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    public void saveWordUserKnowledge(final String wordId, final Assessment assessment) {
        String userId = SecurityUtil.getCurrentUserId();
        Word word = wordRepository.findByWord(wordId).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + wordId));
        UserWordComprehension wordComprehension = userWordComprehensionRepository
                .findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_Word(userId, wordId)
                .orElse(new UserWordComprehension(
                        new UserWordComprehensionID(word.getWord(), userId),
                        1,
                        null,
                        Timestamp.valueOf(LocalDateTime.now()),
                        User.builder().id(userId).build()));
        Integer level = getLevel(assessment, wordComprehension.getWordComprehensionLevel());
        wordComprehension.setWordComprehensionLevel(level);
        // mastered or ignore (no next learning time)
        wordComprehension.setNextLearning((level != 11 && level != 12) ? Timestamp.valueOf(LocalDateTime.now()
                .plusDays(Level.valueOf("LEVEL_" + level).getDay())
                .truncatedTo(ChronoUnit.DAYS)) : null);
        wordComprehension.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
        userWordComprehensionRepository.save(wordComprehension);
    }

    private static Integer getLevel(Assessment assessment, Integer currentLevel) {
        if (assessment == Assessment.EASY) {
            currentLevel = currentLevel + 2;
            if (currentLevel > 10) currentLevel = 11;
        } else if (assessment == Assessment.NORMAL) {
            currentLevel = currentLevel + 1;
            if (currentLevel > 10) currentLevel = 11;
        } else if (assessment == Assessment.HARD) {
            currentLevel = 2;
        } else if (assessment == Assessment.MASTERED) {
            currentLevel = 11;
        } else {
            currentLevel = 12;
        }
        if (currentLevel < 1) {
            throw new InvalidException("Invalid level", "Level after assessment must be greater than 0");
        }
        return currentLevel;
    }

    public FrequencyResponseDTO getWordTest(int page, int size, Long topicId) {
        String userId = SecurityUtil.getCurrentUserId();
        List<FrequencyDTO> comprehension = userWordComprehensionRepository.getWordComprehension(userId, topicId, page, size);
        FrequencyResponseDTO response = new FrequencyResponseDTO();
        response.setPage(page);
        response.setLimit(size);
        response.setTotal(comprehension.get(0).getCount().intValue());
        response.setData(comprehension.stream().peek(frequency -> {
            frequency.setCount(null);
            frequency.setStatus();
        }).collect(Collectors.toList()));
        return response;
    }

    public long countWord() {
        String userId = SecurityUtil.getCurrentUserId();
        return wordRepository.countWordByUserId(userId);
    }

    public void delayLearningWord(String wordId, int day) {
        String userId = SecurityUtil.getCurrentUserId();
        UserWordComprehension wordComprehension = userWordComprehensionRepository
                .findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_Word(userId, wordId)
                .orElseThrow(() -> new InvalidException("Learning word not found",
                        "Not found any learning word with id: " + wordId));
        if (wordComprehension.getNextLearning() == null) {
            throw new InvalidException("Mastered or ignored word",
                    "Word with id " + wordId + " is mastered or ignored");
        }
        wordComprehension.setNextLearning(Timestamp.valueOf(LocalDateTime.now()
                .plusDays(day)
                .truncatedTo(ChronoUnit.DAYS)));
        userWordComprehensionRepository.save(wordComprehension);
    }

    @Transactional
    public void resetLearningProgression() {
        String userId = SecurityUtil.getCurrentUserId();
        userWordComprehensionRepository.deleteAllByUserWordComprehensionID_UserId(userId);
        userRepository.findById(userId).ifPresent(user -> {
            user.setScore(null);
            userRepository.save(user);
        });
    }

    @Transactional
    public void resetLearningProgression(final String wordId) {
        String userId = SecurityUtil.getCurrentUserId();
        userWordComprehensionRepository.deleteAllByUserWordComprehensionID_UserIdAndUserWordComprehensionID_Word(userId, wordId);
    }
}
