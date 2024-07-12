package com.vocahype.repository;

import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.UserWordComprehension;
import com.vocahype.entity.UserWordComprehensionID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserWordComprehensionRepositoryCustom {
    List<FrequencyDTO> getWordComprehension(
            String userId,
            Long topicId,
            Integer page,
            Integer size
    );

}
