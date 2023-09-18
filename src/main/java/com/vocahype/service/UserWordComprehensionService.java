package com.vocahype.service;

import com.vocahype.dto.enumeration.Assessment;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserWordComprehensionService {

    public static final String CURRENT_USER_ID = "abcxyz";
    private final UserWordComprehensionRepository userWordComprehensionRepository;
    private final WordRepository wordRepository;

    public void saveWordUserKnowledge(Long wordId, Assessment assessment) {
        String userId = CURRENT_USER_ID;
        Word word = wordRepository.findById(wordId).orElseThrow(() -> new InvalidException("Word not found", "Not found any word with id: " + wordId));
        UserWordComprehension wordComprehension = userWordComprehensionRepository
                .findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_WordId(userId, wordId)
                .orElse(new UserWordComprehension(
                        new UserWordComprehensionID(wordId, userId),
                        1,
                        null,
                        word));
        Integer level = getLevel(assessment, wordComprehension.getWordComprehensionLevel());
        wordComprehension.setWordComprehensionLevel(level);
        if (level != 11 && level != 12) { // mastered or ignore (no next learning time)
            wordComprehension.setNextLearning(Timestamp.valueOf(LocalDateTime.now()
                    .plusDays(Level.valueOf("LEVEL_" + level).getDay())
                    .truncatedTo(ChronoUnit.DAYS)));
        }
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
}