package com.vocahype.repository;

import com.vocahype.entity.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}
