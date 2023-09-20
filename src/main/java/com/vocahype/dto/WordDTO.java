package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
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

    public WordDTO(Word word) {
        this.id = word.getId();
        this.word = word.getWord();
        this.count = word.getCount();
        this.pos = word.getPos();
        this.point = word.getPoint();
        this.phonetic = word.getPhonetic();
        this.syllable = word.getSyllable();
        this.phoneticStart = word.getPhoneticStart();
        this.phoneticEnd = word.getPhoneticEnd();
        this.createdOn = word.getCreatedOn();
        this.updatedOn = word.getUpdatedOn();
        this.definitions = new HashSet<>();
        this.synonyms = new HashSet<>();
        word.getDefinitions().forEach(definition -> this.definitions.add(new DefinitionDTO(definition)));
        word.getSynonyms().forEach(synonym -> this.synonyms.add(new SynonymDTO(synonym)));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
