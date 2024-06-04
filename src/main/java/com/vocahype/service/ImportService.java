package com.vocahype.service;

import com.vocahype.entity.Word;
import com.vocahype.exception.InvalidException;
import com.vocahype.repository.WordRepository;
import edu.stanford.nlp.ling.CoreLabel;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.PropertiesUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {

    public static final String WORD_REGEX = "^(?!.*'s$)[a-zA-Z']+$";
    private final JdbcTemplate jdbcTemplate;
    private final WordRepository wordRepository;

    @Transactional
    public void importWordInTopic(final MultipartFile file, final Integer topicId) {
        if (file == null) {
            return;
        }
        Map<String, Long> wordFrequency = readWordsFromSrtFile(file);
        if (wordFrequency.isEmpty()) {
            throw new InvalidException("No words found in file", "No words found in file");
        }
        saveWords(wordFrequency.keySet());
        insertMultipleValues(wordFrequency.entrySet(), topicId);
    }

    public static Map<String, Long> extractWords(final String text) {
        Map<String, Long> wordFrequency = new HashMap<>();
        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma"
        );
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        for (CoreLabel token : document.tokens()) {
            String word = token.word();
            if (word.matches(WORD_REGEX) && word.length() > 1) {
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();
                wordFrequency.put(lemma, wordFrequency.getOrDefault(lemma, 0L) + 1);
            }
        }
        return wordFrequency;
    }

    public static Map<String, Long> readWordsFromSrtFile(MultipartFile file) {
        Map<String, Long> wordFrequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (!line.trim().isEmpty()) {
                    if (!isIndexOrTimingLine(line)) {
                        Map<String, Long> lineWordFrequency = extractWords(line);
                        for (Map.Entry<String, Long> entry : lineWordFrequency.entrySet()) {
                            wordFrequency.merge(entry.getKey(), entry.getValue(), Long::sum);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new InvalidException("Error reading file", "Error reading file: " + e.getMessage());
        }
        return wordFrequency;
    }

    private static List<String> extractOriginalWord(String line) {
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

    private void saveWords(Set<String> words) {
        List<Word> existWord = wordRepository.findAllByWordIn(words);
        if (words.size() == existWord.size()) {
            return;
        }
        List<String> existWordList = existWord.stream().map(Word::getWord).collect(Collectors.toList());
        Set<Word> wordList = words.stream().filter(s -> !existWordList.contains(s))
                .map(word -> Word.builder().word(word).build()).collect(Collectors.toSet());
        wordRepository.saveAllAndFlush(wordList);
    }

    public void insertMultipleValues(final Set<Map.Entry<String, Long>> wordFrequencies, Integer topicId) {
        String sql = "INSERT INTO word_topic AS wt (word_id, topic_id, frequency) "
                + "SELECT w.id, ?, ? FROM vh.words w "
                + "            where w.word = ? "
                + "ON CONFLICT (word_id, topic_id) DO UPDATE SET frequency = wt.frequency + ?;";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                Map.Entry<String, Long> wordFrequency = wordFrequencies.stream().skip(i).findFirst().get();
                ps.setInt(1, topicId);
                ps.setLong(2, wordFrequency.getValue());
                ps.setString(3, wordFrequency.getKey());
                ps.setLong(4, wordFrequency.getValue());
            }

            @Override
            public int getBatchSize() {
                return wordFrequencies.size();
            }
        });
    }
}
