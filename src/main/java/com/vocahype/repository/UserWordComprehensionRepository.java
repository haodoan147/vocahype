package com.vocahype.repository;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserWordComprehensionRepository extends JpaRepository<UserWordComprehension, UserWordComprehensionID> {

    Optional<UserWordComprehension> findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_WordId(final String userId, final Long wordId);

    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, true, case when (u.topic.id is not null and u.topic.id = wt.wordTopicID.topicId) then 1 else 0 end) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?1 "
//            + "and uwc.nextLearning is not null "
            + "join User u on u.id = ?1 and (w.id > u.score or u.score is null) "
            + "join WordTopic wt on w.id = wt.wordTopicID.wordId and wt.wordTopicID.topicId = ?2 "
            + "where uwc.wordComprehensionLevel is null or (uwc.wordComprehensionLevel != 11 and uwc.wordComprehensionLevel != 12) "
            + "order by case when uwc.nextLearning <= current_date then 0"
            + "when uwc.nextLearning is null then 1 else 2 end, uwc.nextLearning, wt.frequency desc, w.id")
    Page<WordDTO> findByUserWordComprehensionID_UserIdOrderByNextLearningJoinWordTopic(final String userId,
                                                                                       final Pageable pageable,
                                                                                       final Long topicId);

    @Query("select new com.vocahype.dto.WordDTO(w, false, uwc.nextLearning, uwc.wordComprehensionLevel, true, case when (u.topic.id is not null and u.topic.id = wt.wordTopicID.topicId) then 1 else 0 end) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?1 "
//            + "and uwc.nextLearning is not null "
            + "join User u on u.id = ?1 and (w.id > u.score or u.score is null) "
            + "left join WordTopic wt on w.id = wt.wordTopicID.wordId and wt.wordTopicID.topicId = u.topic.id "
            + "where uwc.wordComprehensionLevel is null or (uwc.wordComprehensionLevel != 11 and uwc.wordComprehensionLevel != 12) "
            + "order by case "
            + "when wt.wordTopicID.topicId is not null and (uwc.nextLearning <= current_date or uwc.nextLearning is null) then 0 "
            + "when uwc.nextLearning <= current_date then 1 "
            + "when uwc.nextLearning is null then 2 "
            + "when wt.wordTopicID.topicId is not null then 3 "
            + "else 4 end, uwc.nextLearning, w.id")
    Page<WordDTO> findByUserWordComprehensionID_UserIdOrderByNextLearning(final String userId, final Pageable pageable);

    void deleteAllByUserWordComprehensionID_UserId(final String userId);

    void deleteAllByUserWordComprehensionID_UserIdAndUserWordComprehensionID_WordId(final String userId,
                                                                                    final Long wordId);

    long countByUserWordComprehensionID_UserIdAndWordComprehensionLevelIn(String userWordComprehensionID_userId,
                                                                          Collection<Integer> wordComprehensionLevel);

    @EntityGraph("graph.userWordComprehension.word")
    List<UserWordComprehension> findByUserWordComprehensionID_UserIdAndUpdateAtAfterAndWordComprehensionLevelNotIn(String userId, Timestamp start, Collection<Integer> wordComprehensionLevel);

    Page<UserWordComprehension> findByUserWordComprehensionID_UserIdAndWordComprehensionLevelInAndWord_WordIgnoreCase(String userId, Collection<Integer> wordComprehensionLevel, String word, Pageable pageable);

    Page<UserWordComprehension> findByUserWordComprehensionID_UserIdAndWordComprehensionLevelInAndWord_WordContainsIgnoreCase(String userId, Collection<Integer> wordComprehensionLevel, String word, Pageable pageable);
}
