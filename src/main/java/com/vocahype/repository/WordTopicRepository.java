package com.vocahype.repository;

import com.vocahype.entity.WordTopic;
import com.vocahype.entity.WordTopicID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface WordTopicRepository extends JpaRepository<WordTopic, WordTopicID> {
    void deleteAllByTopicIdAndWordNotIn(final Long id, final Set<String> collect);
    List<WordTopic> findAllByTopic_Id(final Long id);
}
