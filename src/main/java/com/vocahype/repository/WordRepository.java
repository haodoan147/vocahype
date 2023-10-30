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
    @EntityGraph("graph.WordMeaningPos")
    List<Word> findByWordContainsIgnoreCaseOrderById(String word, final Pageable pageable);

    @Query("select new com.vocahype.dto.WordDTO(w, true, false) from Word w where lower(w.word) = lower(?1)")
    List<WordDTO> findByWordIgnoreCaseOrderById(String word, final Pageable pageable);

    List<Word> findByCountIsNotNullAndIdGreaterThanOrderById(final Long id, final Pageable pageable);

    long countByWordContainsIgnoreCase(String word);

    long countByWordIgnoreCase(String word);
//    @EntityGraph("graph.WordSynonymSynonym")

//    @EntityGraph("graph.SynonymWord")
    @Query("select new com.vocahype.dto.WordDTO(w, true, true) from Word w "
//            + "left join Synonym s on w.id = s.synonym.id and s.isSynonym = true "
//            + "left join Synonym a on w.id = a.synonym.id and a.isSynonym = false "
            + "where w.id = ?1 ")
    Optional<WordDTO> findWordDTOById(Long aLong);

    Optional<Word> findByWordIgnoreCaseOrderById(String word);
}
