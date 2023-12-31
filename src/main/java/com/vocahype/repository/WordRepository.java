package com.vocahype.repository;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
//    @EntityGraph("graph.WordMeaningPos")
//    List<Word> findByWordContainsIgnoreCaseOrderById(String word, final Pageable pageable);
    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, false) "
            + "from Word w "
            + "join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?2 "
            + "and uwc.wordComprehensionLevel in ?3 "
            + "where lower(w.word) like lower(concat('%', ?1, '%'))"
            + "order by w.id")
    List<WordDTO> findByWordContainsIgnoreCaseAndUserWordComprehensionsOrderById(String word, String userId,
                                                                              List<Integer> levels, final Pageable pageable);

    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, false) "
            + "from Word w "
            + "join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?2 "
            + "and uwc.wordComprehensionLevel in ?3 "
            + "where lower(w.word) = lower(?1)"
            + "order by w.id")
    List<WordDTO> findByWordIgnoreCaseAndUserWordComprehensionsOrderById(String word, String userId,
                                                                         List<Integer> levels, final Pageable pageable);

    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, false) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?2 "
            + "where lower(w.word) like lower(concat('%', ?1, '%'))"
            + "order by w.id")
    List<WordDTO> findByWordContainsIgnoreCaseOrderById(String word, String userId, final Pageable pageable);

    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, false) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?2 "
            + "where lower(w.word) = lower(?1)"
            + "order by w.id")
    List<WordDTO> findByWordIgnoreCaseOrderById(String word, String userId, final Pageable pageable);

    List<Word> findByCountIsNotNullAndIdGreaterThanOrderById(final Long id, final Pageable pageable);

    long countByWordContainsIgnoreCase(String word);

    long countByWordIgnoreCase(String word);

    @Query("select count(w) from Word w " +
            "where w.id > (select u.score from User u where u.id = ?1)")
    long countWordByUserId(String userId);
//    @EntityGraph("graph.WordSynonymSynonym")

//    @EntityGraph("graph.SynonymWord")
    @Query("select new com.vocahype.dto.WordDTO(w, true, uwc.nextLearning, uwc.wordComprehensionLevel, true) from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?2 "
            + "where w.id = ?1 ")
    Optional<WordDTO> findWordDTOById(Long aLong, String userId);

    Optional<Word> findByWordIgnoreCaseOrderById(String word);
}
