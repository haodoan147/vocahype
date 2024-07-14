package com.vocahype.repository;

import com.vocahype.dto.WordUserKnowledgeDTO;
import com.vocahype.entity.WordUserKnowledge;
import com.vocahype.entity.WordUserKnowledgeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordUserKnowledgeRepository extends JpaRepository<WordUserKnowledge, WordUserKnowledgeID> {

    @Query("select new com.vocahype.dto.WordUserKnowledgeDTO(w.id, w.word) "
            + "from WordUserKnowledge wuk "
            + "join Word w on w.word = wuk.wordUserKnowledgeID.word "
            + "where wuk.wordUserKnowledgeID.userId = ?1")
    List<WordUserKnowledgeDTO> findAllByWordUserKnowledgeID_UserId(String currentUserId);
}
