package com.vocahype.repository;

import com.vocahype.dto.TopicDTO;
import com.vocahype.entity.Topic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {

    @Query("select count(wt) " +
            "from WordTopic wt " +
            "join Word w on wt.topic.id = ?1 and wt.word.id = w.id " +
            "join UserWordComprehension uwc on uwc.userWordComprehensionID.word = w.word and uwc.user.id = ?2 and uwc.wordComprehensionLevel = ?3" )
    int countLearningWordTopicsByTopicId(Long topicId, String userId, Integer wordComprehensionLevel);

    @Query("select count(wt) " +
            "from WordTopic wt " +
            "join Word w on wt.topic.id = ?1 and wt.word.id = w.id " +
            "join UserWordComprehension uwc on uwc.userWordComprehensionID.word = w.word and uwc.user.id = ?2 and uwc.wordComprehensionLevel > ?3 and uwc.wordComprehensionLevel < ?4" )
    int countLearningWordTopicsByTopicIdBetween(Long topicId, String userId, Integer wordComprehensionLevelStart, Integer wordComprehensionLevelEnd);

    Optional<Topic> findByName(String name);

    @EntityGraph(value = "graph.topic.wordTopics.word")
    Optional<Topic> findFirstById(Long id);

    @Query(value = "select com. from vh.topics t " +
            "join vh.word_topic wt ON t.id = wt.topic_id " +
            "join vh.words w ON w.word = wt.word;", nativeQuery = true)
    List<TopicDTO> findAllTopic();
}
