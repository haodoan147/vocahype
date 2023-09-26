package com.vocahype.repository;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
//    @EntityGraph("graph.WordSynonymSynonym")
    List<Word> findByWordContainsIgnoreCase(String word, final Pageable pageable);

    @Query("select new com.vocahype.dto.WordDTO(w) from Word w where lower(w.word) = lower(?1)")
    List<WordDTO> findByWordIgnoreCase(String word, final Pageable pageable);

    long countByWordContainsIgnoreCase(String word);

    long countByWordIgnoreCase(String word);
//    @EntityGraph("graph.WordSynonymSynonym")
    @Override
    Optional<Word> findById(Long aLong);
}
