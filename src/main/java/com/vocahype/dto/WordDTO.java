package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Pos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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
    private Set<DefinitionDTO> definitions;
    @JsonIgnore
    private Set<SynonymDTO> synonyms;

    @Override
    public String toString() {
        return id.toString();
    }
}
