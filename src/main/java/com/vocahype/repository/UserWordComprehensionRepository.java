package com.vocahype.repository;

import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWordComprehensionRepository extends JpaRepository<UserWordComprehension, UserWordComprehensionID> {

    Optional<UserWordComprehension> findByUserWordComprehensionID_UserIdAndUserWordComprehensionID_WordId(final String userId, final Long wordId);
}
