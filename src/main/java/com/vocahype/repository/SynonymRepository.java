package com.vocahype.repository;

import com.vocahype.entity.Synonym;
import com.vocahype.entity.SynonymID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SynonymRepository extends JpaRepository<Synonym, SynonymID> {
}
