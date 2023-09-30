package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Pos;
import com.vocahype.entity.Synonym;
import com.vocahype.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private ComprehensionDTO comprehension;
    @JsonIgnore
    private Set<DefinitionDTO> definitions;
    @JsonIgnore
    private Set<SynonymDTO> synonyms;

    public WordDTO(Word word, boolean isContainDefinitions, String status, Date dueDate,
                   Integer comprehensionLevel, List<Synonym> synonyms, List<Synonym> antonyms) {
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
        if (status != null && dueDate != null && comprehensionLevel != null) {
            this.comprehension = new ComprehensionDTO(status, dueDate, comprehensionLevel);
        }
        this.definitions = new HashSet<>();
        this.synonyms = new HashSet<>();
        if (isContainDefinitions) {
            word.getDefinitions().forEach(definition -> this.definitions.add(new DefinitionDTO(definition)));
        }
        word.getSynonyms().forEach(synonym -> this.synonyms.add(new SynonymDTO(synonym)));
        if (synonyms != null) {
            this.synonyms.addAll(synonyms.stream().map(synonym -> new SynonymDTO(synonym.getWord().getId(),
                    synonym.getWord().getWord(), true)).collect(Collectors.toSet()));
        }
        if (antonyms != null) {
            this.synonyms.addAll(antonyms.stream().map(synonym -> new SynonymDTO(synonym.getWord().getId(),
                    synonym.getWord().getWord(), false)).collect(Collectors.toSet()));
        }
//        this.synonyms.addAll(synonyms.stream().map(synonym -> new SynonymDTO(synonym.getWord().getId(),
//                synonym.getWord().getWord(), true)).collect(Collectors.toSet()));
//        this.synonyms.addAll(antonyms.stream().map(synonym -> new SynonymDTO(synonym.getWord().getId(),
//                synonym.getWord().getWord(), false)).collect(Collectors.toSet()));
//        this.synonyms.addAll(synonyms.stream().map(synonymDTO -> synonymDTO.setIsSynonym(true)).collect(Collectors.toSet()));
//        this.synonyms.addAll(antonyms.stream().map(synonymDTO -> synonymDTO.setIsSynonym(false)).collect(Collectors.toSet()));
//        if (synonyms != null) {
//            this.synonyms.add(new SynonymDTO(synonyms.getWord().getId(),
//                    synonyms.getWord().getWord(), true));
//        }
//        if (antonyms != null) {
//            this.synonyms.add(new SynonymDTO(antonyms.getWord().getId(),
//                    antonyms.getWord().getWord(), false));
//        }
    }

    public WordDTO(Word word, List<Synonym> synonyms, List<Synonym> antonyms) {
        this(word, false, null, null, null, synonyms, antonyms);
    }

    public WordDTO(Word word, Synonym synonyms, Synonym antonyms) {
        this(word, false, null, null, null, List.of(synonyms), List.of(antonyms));
    }

    public WordDTO(Word word) {
        this(word, false, null, null, null);
    }

    public WordDTO(Word word, boolean isContainDefinitions, String status, Date dueDate, Integer comprehensionLevel) {
        this(word, isContainDefinitions, status, dueDate, comprehensionLevel, null, null);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
