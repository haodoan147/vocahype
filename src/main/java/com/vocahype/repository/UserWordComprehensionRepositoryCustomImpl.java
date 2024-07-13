package com.vocahype.repository;

import com.vocahype.dto.ComprehensionDTO;
import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.dto.enumeration.TransformerType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserWordComprehensionRepositoryCustomImpl extends BaseRepository implements UserWordComprehensionRepositoryCustom {

    @Override
    public List<FrequencyDTO> getWordComprehension(final String userId, final Long topicId, Integer page, Integer size) {
//        String sql = "SELECT * FROM learnings.user_word_comprehension uwc " +
//                "JOIN vh.frequency f ON uwc.word = f.word " +
//                "JOIN vh.users u ON uwc.user_id = u.id AND (f.id > u.score OR u.score IS NULL) AND f.id > 220 " +
//                (topicId != null && topicId != 17 ? "JOIN vh.word_topics wt ON w.id = wt.word_id AND wt.topic_id = ? " : "") +
//                "WHERE uwc.word_comprehension_level IS NULL OR (uwc.word_comprehension_level != 11 AND uwc.word_comprehension_level != 12) " +
//                "ORDER BY CASE WHEN uwc.next_learning <= CURRENT_DATE THEN 0 " +
//                "WHEN uwc.next_learning IS NULL THEN 1 ELSE 2 END, uwc.next_learning, wt.frequency DESC, w.id";
        String sql = "SELECT f.lemma as word, MIN(f.frequency) AS frequency, count(*) over () AS count, uwc.next_learning as \"dueDate\", uwc.word_comprehension_levels_id as level, wt.topic_id IS NOT NULL AND u.topic_id IS NOT NULL AND wt.topic_id = u.topic_id \"isInTopic\" " +
                "FROM vh.frequency f " +
                "         LEFT JOIN learning.user_word_comprehension uwc ON uwc.word = f.lemma AND uwc.user_id = :userId " +
                "         JOIN vh.users u ON u.id = :userId " +
                "         LEFT JOIN vh.word_topic wt ON f.lemma = wt.word "+
                "WHERE (uwc.word_comprehension_levels_id IS NULL OR " +
                "       (uwc.word_comprehension_levels_id != 11 AND uwc.word_comprehension_levels_id != 12)) " +
                "  AND (u.score IS NULL OR f.id > u.score) " +
                "  AND f.id > 220 " +
                (topicId != null ? " AND wt.topic_id = :topicId AND wt.topic_id IS NOT NULL " : " ") +
                "GROUP BY f.lemma, uwc.next_learning, uwc.word_comprehension_levels_id, u.topic_id, wt.topic_id " +
                "ORDER BY CASE WHEN u.topic_id IS NOT NULL AND wt.topic_id = u.topic_id THEN 0 " +
                "        WHEN uwc.next_learning <= CURRENT_DATE THEN 1 " +
                "        WHEN uwc.next_learning IS NULL THEN 2 ELSE 3 END, " +
                "        uwc.next_learning, frequency DESC LIMIT :size OFFSET :page * :size ;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("page", page);
        parameters.put("size", size);
        if (topicId != null) {
            parameters.put("topicId", topicId);
        }
        return getRecords(sql, parameters, FrequencyDTO.class);
    }

    @Override
    public List<FrequencyDTO> getRandomWords(Integer size) {
        String sql = "SELECT lemma word FROM vh.frequency WHERE id > 220 ORDER BY RANDOM() LIMIT :size";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("size", size);
        return getRecords(sql, parameters, FrequencyDTO.class);
    }

    @Override
    public List<FrequencyDTO> getRandomWordsNotIn(Integer size, Collection<String> words) {
        String sql = "SELECT lemma word FROM vh.frequency WHERE id > 220 AND lemma NOT IN (:words) ORDER BY RANDOM() LIMIT :size";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("size", size);
        parameters.put("words", words);
        return getRecords(sql, parameters, FrequencyDTO.class);
    }


}
