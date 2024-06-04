package com.vocahype.repository;

import com.vocahype.entity.Pos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosRepository extends JpaRepository<Pos, String> {
}
