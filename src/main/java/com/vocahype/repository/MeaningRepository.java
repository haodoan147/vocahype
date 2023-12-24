package com.vocahype.repository;

import com.vocahype.entity.Meaning;
import com.vocahype.entity.UserLearningGoalTracking;
import com.vocahype.entity.UserLearningGoalTrackingID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {

    @Modifying
    @Query(value = "insert into vh.meanings (pos_id, word_id) values (?1, ?2) ;", nativeQuery = true)
    Integer insertByPosIdAndWordId(String posId, Long wordId);

    @Modifying
    @Query(value = "DELETE FROM vh.meanings where id = ?1 ", nativeQuery = true)
    void deleteAllById(Long aLong);
}
