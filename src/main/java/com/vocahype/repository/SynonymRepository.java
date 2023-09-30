package com.vocahype.repository;

import com.vocahype.dto.SynonymDTO;
import com.vocahype.entity.Synonym;
import com.vocahype.entity.SynonymID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SynonymRepository extends JpaRepository<Synonym, SynonymID> {

    @Query("select new com.vocahype.dto.SynonymDTO(s.synonymID.wordId, s.word.word, s.isSynonym) from Synonym s "
            + "where s.synonymID.synonymId = ?1 and s.isSynonym = ?2")
//    @Query(" with RECURSIVe reselect s from Synonym s where s.synonymID.synonymId = ?1 and s.isSynonym = true "
//            + "union "
//            + "select s from Synonym s where s.synonymID.wordId = ?1 and s.isSynonym = false ")
    List<SynonymDTO> findWordSynonymDTOBySynonymID_SynonymIdAndIsSynonym(Long synonymId, Boolean isSynonym);

    @Query("select new com.vocahype.dto.SynonymDTO(s) from Synonym s "
            + "where s.synonymID.synonymId = ?1 and s.isSynonym = ?2")
//    @Query(" with RECURSIVe reselect s from Synonym s where s.synonymID.synonymId = ?1 and s.isSynonym = true "
//            + "union "
//            + "select s from Synonym s where s.synonymID.wordId = ?1 and s.isSynonym = false ")
    List<SynonymDTO> findSynonymDTOBySynonymID_SynonymIdAndIsSynonym(Long synonymId, Boolean isSynonym);

    @Query("select new com.vocahype.dto.SynonymDTO(s) from Synonym s "
            + "where s.synonymID.wordId = ?1 and s.isSynonym = ?2")
    List<SynonymDTO> findSynonymDTOBySynonymID_WordIdAndIsSynonym(Long wordId, Boolean isSynonym);
}
