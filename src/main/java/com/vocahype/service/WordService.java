package com.vocahype.service;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.User;
import com.vocahype.entity.Word;
import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import com.vocahype.repository.WordRepository;
import com.vocahype.repository.WordUserKnowledgeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.vocahype.util.Constants.CURRENT_USER_ID;
import static com.vocahype.util.Constants.WORD_COUNT;

@Service
@RequiredArgsConstructor
public class WordService {

    public static final int BIAS = 10;
    private final WordRepository wordRepository;
    private final WordUserKnowledgeRepository wordUserKnowledgeRepository;

    public List<Word> getWordList() {
        List<Word> wordList = new ArrayList<>();
        LongRange[] ranges = {
                new LongRange(0L, WORD_COUNT / BIAS, 10),
                new LongRange(WORD_COUNT / BIAS, WORD_COUNT / BIAS * 2, 9),
                new LongRange(WORD_COUNT / BIAS * 2, WORD_COUNT / BIAS * 3, 7),
                new LongRange(WORD_COUNT / BIAS * 3, WORD_COUNT / BIAS * 4, 6),
                new LongRange(WORD_COUNT / BIAS * 4, WORD_COUNT / BIAS * 5, 5),
                new LongRange(WORD_COUNT / BIAS * 5, WORD_COUNT / BIAS * 6, 4),
                new LongRange(WORD_COUNT / BIAS * 6, WORD_COUNT / BIAS * 7, 3),
                new LongRange(WORD_COUNT / BIAS * 7, WORD_COUNT / BIAS * 8, 3),
                new LongRange(WORD_COUNT / BIAS * 8, WORD_COUNT / BIAS * 9, 2),
                new LongRange(WORD_COUNT / BIAS * 9, WORD_COUNT / BIAS, 1)
        };
        for (LongRange range : ranges) {
            int numRecordsToFetch = range.getNumRecords();
            for (int i = 0; i < numRecordsToFetch; i++) {
                long randomId = getRandomNumberInRange(range.getMinId(), range.getMaxId());
                Optional<Word> word = wordRepository.findById(randomId);
                if (word.isPresent()) {
                    wordList.add(word.get());
                } else {
                    i--;
                }
            }
        }
        return wordList;
    }

    private long getRandomNumberInRange(long min, long max) {
        Random random = new Random();
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

    public void checkUserKnowledge(final List<WordDTO> wordDTO) {
        Set<WordUserKnowledge> knownWords = new HashSet<>();
        wordDTO.forEach(word -> {
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

    @AllArgsConstructor
    @Getter
    private static class LongRange {
        private long minId;
        private long maxId;
        private int numRecords;
    }
}
