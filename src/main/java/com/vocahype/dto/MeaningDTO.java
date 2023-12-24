package com.vocahype.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Meaning;
import com.vocahype.entity.Pos;
import com.vocahype.entity.Synonym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeaningDTO {
    private Long id;
    @JsonIgnore
    private Pos pos;
    @JsonIgnore
    private Set<DefinitionDTO> definitions;
    @JsonIgnore
    private Set<SynonymDTO> synonyms;

    public MeaningDTO(Meaning meaning, boolean isContainDefinitions, List<Synonym> synonyms, List<Synonym> antonyms) {
        this.id = meaning.getId();
        this.pos = meaning.getPos();
        this.definitions = new HashSet<>();
        this.synonyms = new HashSet<>();
        if (isContainDefinitions) {
            meaning.getDefinitions().forEach(definition -> this.definitions.add(new DefinitionDTO(definition)));
            meaning.getSynonyms().forEach(synonym -> this.synonyms.add(new SynonymDTO(synonym)));
        }
        if (synonyms != null) {
            this.synonyms.addAll(synonyms.stream().map(synonym -> new SynonymDTO(synonym.getMeaning().getWord().getId(),
                    synonym.getMeaning().getWord().getWord(), true)).collect(Collectors.toSet()));
        }
        if (antonyms != null) {
            this.synonyms.addAll(antonyms.stream().map(synonym -> new SynonymDTO(synonym.getMeaning().getWord().getId(),
                    synonym.getMeaning().getWord().getWord(), false)).collect(Collectors.toSet()));
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

    public MeaningDTO(Meaning meaning, List<Synonym> synonyms, List<Synonym> antonyms) {
        this(meaning, false, synonyms, antonyms);
    }

    public MeaningDTO(Meaning meaning, Synonym synonyms, Synonym antonyms) {
        this(meaning, false, List.of(synonyms), List.of(antonyms));
    }

    public MeaningDTO(Meaning meaning) {
        this(meaning, false);
    }

    public MeaningDTO(Meaning meaning, boolean isContainDefinitions) {
        this(meaning, isContainDefinitions, null, null);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
