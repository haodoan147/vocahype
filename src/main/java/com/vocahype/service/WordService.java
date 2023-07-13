package com.vocahype.service;

import com.vocahype.entity.Word;
import com.vocahype.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.vocahype.util.Constants.WORD_COUNT;

@Service
@RequiredArgsConstructor
public class WordService {

    public static final int BIAS = 1000;
    private final WordRepository wordRepository;

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
                Long randomId = new Long(getRandomNumberInRange(range.getMinId(), range.getMaxId()));
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

    @AllArgsConstructor
    @Getter
    private static class LongRange {
        private long minId;
        private long maxId;
        private int numRecords;
    }
}
