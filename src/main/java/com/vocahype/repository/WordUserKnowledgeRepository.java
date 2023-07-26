package com.vocahype.repository;

import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WordUserKnowledgeRepository extends JpaRepository<WordUserKnowledge, WordUserKnowledgeID> {

    @Modifying
    @Query("delete from WordUserKnowledge where wordUserKnowledgeID.userId = ?1")
    void deleteAllByWordUserKnowledgeID_UserId(Long userId);
}
