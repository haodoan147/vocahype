package com.vocahype.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "synonyms", schema = "vh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Synonym {
    @EmbeddedId
    private SynonymID synonymID;
    @Column(name = "is_synonym")
    private Boolean isSynonym;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wordId")
    @JoinColumn(name = "word_id")
    private Word word;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("synonymId")
    @JoinColumn(name = "synonym_id")
    private Word synonym;

    @Override
    public String toString() {
        return synonym.getWord();
    }
}
