package com.vocahype.repository;

import com.vocahype.dto.SynonymDTO;
import com.vocahype.entity.Synonym;
import com.vocahype.entity.SynonymID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SynonymRepository extends JpaRepository<Synonym, SynonymID> {

    @Query("select new com.vocahype.dto.SynonymDTO(s.meaning.word.id, s.synonym.word, s.isSynonym) from Synonym s "
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
            + "where s.meaning.word.id = ?1 and s.isSynonym = ?2")
    List<SynonymDTO> findSynonymDTOBySynonymID_WordIdAndIsSynonym(Long wordId, Boolean isSynonym);

    void deleteAllByMeaning_Id(Long id);

    @Modifying
    @Query(value = "insert into vh.synonyms (synonym_id, is_synonym, meanings_id) VALUES (?1, ?2, ?3) ON CONFLICT DO NOTHING ;", nativeQuery = true)
    void insertSynonym(Long synonymId, Boolean isSynonym, Long meaningsId);
}
