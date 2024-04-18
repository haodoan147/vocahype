package com.vocahype.service;

import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final JdbcTemplate jdbcTemplate;
    private final WordRepository wordRepository;

    @Transactional
    public void importWordInTopic(final MultipartFile file, final Integer topicId) {
        if (file == null) {
            return;
        }
        try {
            List<String> wordLines = readWordsFromSrtFile(file);
            if (wordLines.isEmpty()) {
                throw new InvalidException("No words found in file", "No words found in file");
            }
            saveAndGetWords(wordLines);
            insertMultipleValues(wordLines, topicId);
        } catch (IOException e) {
            throw new InvalidException("Error reading file", "Error reading file: " + e.getMessage());
        }
    }
    public static List<String> readWordsFromSrtFile(MultipartFile file) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (!line.trim().isEmpty()) {
                    if (!isIndexOrTimingLine(line)) {
                        words.addAll(extractWords(line));
                    }
                }
            }
        }
        return words;
    }

    private static List<String> extractWords(String line) {
        String[] words = line.split("[^a-zA-Z']");
        List<String> wordList = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                wordList.add(word.toLowerCase());
            }
        }
        return wordList;
    }

    private static boolean isIndexOrTimingLine(String line) {
        return line.matches("^\\d+$") || line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}$");
    }

    private List<Word> saveAndGetWords(List<String> words) {
        Set<String> wordsSet = new HashSet<>(words);
        List<Word> allByWord = wordRepository.findAllByWordIn(wordsSet);
        Set<String> existWords = allByWord.stream().map(Word::getWord).collect(Collectors.toSet());
        if (existWords.size() == wordsSet.size()) {
            return allByWord;
        }
        Set<Word> wordList = wordsSet.stream().filter(s -> !existWords.contains(s))
                .map(word -> Word.builder().word(word).build()).collect(Collectors.toSet());
        return wordRepository.saveAllAndFlush(wordList);
    }

    public void insertMultipleValues(final List<String> words, Integer topicId) {
        String sql = "INSERT INTO word_topic AS wt (word_id, topic_id, frequency) "
                + "SELECT w.id, ?, 1 FROM vh.words w "
                + "            where w.word = ? "
                + "ON CONFLICT (word_id, topic_id) DO UPDATE SET frequency = wt.frequency + 1;";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                String word = words.get(i);
                ps.setInt(1, topicId);
                ps.setString(2, word);
            }

            @Override
            public int getBatchSize() {
                return words.size();
            }
        });
    }
}
