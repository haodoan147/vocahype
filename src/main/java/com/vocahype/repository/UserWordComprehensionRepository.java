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
            + "case when uwc.userWordComprehensionID.userId is null then 'to learn' else 'learning' end, uwc.nextLearning) "
            + "from Word w "
            + "left join UserWordComprehension uwc on w.id = uwc.userWordComprehensionID.wordId "
            + "and uwc.userWordComprehensionID.userId = ?1 "
            + "and uwc.nextLearning is not null "
            + "order by case when uwc.userWordComprehensionID.userId is null then 1 else 0 end, uwc.nextLearning, w.id")
    List<WordDTO> findByUserWordComprehensionID_UserIdOrderByNextLearning(final String userId, final Pageable pageable);
}
