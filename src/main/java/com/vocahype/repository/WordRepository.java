package com.vocahype.repository;

import com.vocahype.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByWordContainsIgnoreCase(String word);
}
