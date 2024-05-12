package com.vocahype.repository;

import com.vocahype.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("select count(wt) " +
            "from WordTopic wt " +
            "join Word w on wt.topic.id = ?1 and wt.word.id = w.id " +
            "join UserWordComprehension uwc on uwc.word.id = w.id and uwc.user.id = ?2 and uwc.wordComprehensionLevel = ?3" )
    int countLearningWordTopicsByTopicId(Long topicId, String userId, Integer wordComprehensionLevel);

    @Query("select count(wt) " +
            "from WordTopic wt " +
            "join Word w on wt.topic.id = ?1 and wt.word.id = w.id " +
            "join UserWordComprehension uwc on uwc.word.id = w.id and uwc.user.id = ?2 and uwc.wordComprehensionLevel > ?3 and uwc.wordComprehensionLevel < ?4" )
    int countLearningWordTopicsByTopicIdBetween(Long topicId, String userId, Integer wordComprehensionLevelStart, Integer wordComprehensionLevelEnd);

    Optional<Topic> findByName(String name);
}
