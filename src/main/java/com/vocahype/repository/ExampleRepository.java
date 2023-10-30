package com.vocahype.repository;

import com.vocahype.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExampleRepository extends JpaRepository<Example, Long> {
    void deleteAllByDefinition_Meaning_Id(Long aLong);

    @Modifying
    @Query(value = "insert into vh.examples (definition_id, example) values (?1, ?2);", nativeQuery = true)
    void insertExample(Long definitionId, String example);
}
