package com.vocahype.repository;

import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordUserKnowledgeRepository extends JpaRepository<WordUserKnowledge, WordUserKnowledgeID> {

    void deleteAllByWordUserKnowledgeID_UserId(Long userId);
}
