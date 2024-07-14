package com.vocahype.dto.request.wordsapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.dto.CustomList;
import com.vocahype.dto.DefinitionDTO;
import com.vocahype.dto.MeaningDTO;
import com.vocahype.dto.SynonymDTO;
import com.vocahype.entity.Pos;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Result {
    private String definition;
    private String partOfSpeech;
    private List<String> synonyms;
    private List<String> typeOf;
    @JsonIgnore
    private List<String> hasTypes;
    private List<String> similarTo;
    private List<String> also;
    private List<String> antonyms;
    private List<String> examples;
    private List<String> derivation;
    private List<String> inCategory;

    public MeaningDTO toMeaningDTO() {
        MeaningDTO meaningDTO = new MeaningDTO();
        CustomList<DefinitionDTO> definitions = new CustomList<>();
        definitions.add(new DefinitionDTO(this.definition, this.examples));
        meaningDTO.setDefinitions(definitions);
        meaningDTO.setPos(new Pos(this.partOfSpeech, null));
        Set<SynonymDTO> synonyms = new HashSet<>();
        if (this.synonyms != null) {
            this.synonyms.forEach(synonym -> synonyms.add(new SynonymDTO(null, synonym, true)));
        }
        if (this.antonyms != null) {
            this.antonyms.forEach(antonym -> synonyms.add(new SynonymDTO(null, antonym, false)));
        }
        if (this.similarTo != null) {
            this.similarTo.forEach(similar -> synonyms.add(new SynonymDTO(null, similar, true)));
        }
        meaningDTO.setSynonyms(synonyms);
        return meaningDTO;
    }
}
