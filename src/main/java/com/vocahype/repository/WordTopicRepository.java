package com.vocahype.repository;

import com.vocahype.entity.WordTopic;
import com.vocahype.entity.WordTopicID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WordTopicRepository extends JpaRepository<WordTopic, WordTopicID> {
}
