package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Definition;
import com.vocahype.entity.Pos;
import com.vocahype.entity.Synonym;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {
    private Long id;
    private String word;
    private Long count;
    @JsonIgnore
    private Pos pos;
    private Double point;
    private String phonetic;
    private Integer syllable;
    private String phoneticStart;
    private String phoneticEnd;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    @JsonIgnore
    private List<Definition> definitions;
    @JsonIgnore
    private List<SynonymDTO> synonyms;

    @Override
    public String toString() {
        return id.toString();
    }
}
