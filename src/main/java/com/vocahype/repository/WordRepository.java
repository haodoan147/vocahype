package com.vocahype.repository;

import com.vocahype.entity.Word;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    @EntityGraph("graph.WordSynonymSynonym")
    List<Word> findByWordContainsIgnoreCase(String word);

    @EntityGraph("graph.WordSynonymSynonym")
    @Override
    Optional<Word> findById(Long aLong);
}
