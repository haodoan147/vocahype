package com.vocahype.repository;

import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.TopicDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.dto.enumeration.TransformerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TopicRepositoryCustomImpl extends BaseRepository implements TopicRepositoryCustom {

    @Override
    public List<TopicDTO> getAll(String userId, Long topicId) {
        String sql = "select t.id, t.description, t.emoji, t.name, count(wt.word) \"wordCount\", count(uwc_learning.word) \"learningWordCount\", count(uwc_mastered.word) \"masteredWordCount\" " +
                "from vh.topics t " +
                "    left join vh.word_topic wt ON t.id = wt.topic_id " +
                "    left join learning.user_word_comprehension uwc_mastered ON uwc_mastered.word = wt.word AND uwc_mastered.word_comprehension_levels_id = 11 AND uwc_mastered.user_id = :userId " +
                "    left join learning.user_word_comprehension uwc_learning ON uwc_learning.word = wt.word AND uwc_learning.word_comprehension_levels_id > 2 AND uwc_learning.word_comprehension_levels_id < 11 AND uwc_mastered.user_id = :userId " +
                (topicId != null ? " where t.id = :topicId " : "") +
                "group by t.id, t.description, t.emoji, t.name order by t.id ;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        if (topicId != null) {
            parameters.put("topicId", topicId);
        }
        return getRecords(sql, parameters, TopicDTO.class, TransformerType.NESTED_BEAN);
    }

    @Override
    public List<WordDTO> getWordInTopic(Long topicId) {
        String sql = "select wt.word from vh.word_topic wt where wt.topic_id = :topicId group by wt.word ;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("topicId", topicId);
        return getRecords(sql, parameters, WordDTO.class, TransformerType.NESTED_BEAN);
    }

    @Override
    public List<WordUserKnowledgeDTO> getRandomWordInTopic(int size) {
        String sql = "select f.lemma word from vh.frequency f group by f.lemma order by random() limit :size ;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("size", size);
        return getRecords(sql, parameters, WordUserKnowledgeDTO.class, TransformerType.NESTED_BEAN);
    }

    @Override
    public List<FrequencyDTO> getFrequencyByWordIn(List<String> words) {
        String sql = "select f.lemma word, f.id as frequency from vh.frequency f where f.lemma in (:words) ;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("words", words);
        return getRecords(sql, parameters, FrequencyDTO.class, TransformerType.NESTED_BEAN);
    }
}
