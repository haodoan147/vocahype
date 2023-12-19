package com.vocahype.repository;

import com.vocahype.dto.WordDTO;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserWordComprehensionRepository extends JpaRepository<UserWordComprehension, UserWordComprehensionID> {

    Optional<UserWordComprehension> findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_WordId(final String userId, final Long wordId);

    @Query("select new com.vocahype.dto.WordDTO(w, false, "
            + "case when uwc.userWordComprehensionID.userId is null then 'to learn' else 'learning' end, uwc.nextLearning, uwc.wordComprehensionLevel, true) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?1 "
//            + "and uwc.nextLearning is not null "
            + "join User u on u.id = ?1 and w.id > u.score "
            + "join WordTopic wt on w.id = wt.wordTopicID.wordId and wt.wordTopicID.topicId = ?2 "
            + "where uwc.wordComprehensionLevel is null or (uwc.wordComprehensionLevel != 11 and uwc.wordComprehensionLevel != 12) "
            + "order by case when uwc.nextLearning <= current_date then 0"
            + "when uwc.nextLearning is null then 1 else 2 end, uwc.nextLearning, wt.frequency desc, w.id")
    List<WordDTO> findByUserWordComprehensionID_UserIdOrderByNextLearningJoinWordTopic(final String userId,
                                                                                      final Pageable pageable,
                                                                                      final Long topicId);

    @Query("select new com.vocahype.dto.WordDTO(w, false, "
            + "case when uwc.userWordComprehensionID.userId is null then 'to learn' else 'learning' end, uwc.nextLearning, uwc.wordComprehensionLevel, true) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?1 "
//            + "and uwc.nextLearning is not null "
            + "join User u on u.id = ?1 and w.id > u.score "
            + "left join WordTopic wt on w.id = wt.wordTopicID.wordId and wt.wordTopicID.topicId = u.topic.id "
            + "where uwc.wordComprehensionLevel is null or (uwc.wordComprehensionLevel != 11 and uwc.wordComprehensionLevel != 12) "
            + "order by case when wt.topic.id is null then 1 else 0 end, case when uwc.nextLearning <= current_date then 0"
            + "when uwc.nextLearning is null then 1 else 2 end, uwc.nextLearning, w.id")
    List<WordDTO> findByUserWordComprehensionID_UserIdOrderByNextLearning(final String userId, final Pageable pageable);
}
