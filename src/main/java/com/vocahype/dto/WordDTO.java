package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
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
    private Double point;
    private String phonetic;
    private Integer syllable;
    private String phoneticStart;
    private String phoneticEnd;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    @JsonIgnore
    private ComprehensionDTO comprehension;
    @JsonIgnore
    private Set<MeaningDTO> meanings;

    public WordDTO(Word word, boolean isContainMeaning, Date dueDate,
                   Integer comprehensionLevel, boolean isContainDefinition) {
        this.id = word.getId();
        this.word = word.getWord();
        this.count = word.getCount();
        this.point = word.getPoint();
        this.phonetic = word.getPhonetic();
        this.syllable = word.getSyllable();
        this.phoneticStart = word.getPhoneticStart();
        this.phoneticEnd = word.getPhoneticEnd();
        this.createdOn = word.getCreatedOn();
        this.updatedOn = word.getUpdatedOn();
        this.comprehension = new ComprehensionDTO(dueDate, comprehensionLevel, word.getId());
        this.meanings = new HashSet<>();
        if (isContainMeaning) {
            word.getMeanings().forEach(meaning -> this.meanings.add(new MeaningDTO(meaning, isContainDefinition)));
        }
    }

    public WordDTO(Word word) {
        this(word, false, null, null, false);
    }

    public WordDTO(Word word, boolean isContainMeaning, boolean isContainDefinition) {
        this(word, isContainMeaning, null, null, isContainDefinition);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
