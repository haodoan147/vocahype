package com.vocahype.dto;

import com.vocahype.entity.Synonym;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SynonymDTO {
    private Long id;
    private String synonym;
    private Boolean isSynonym;

    public SynonymDTO(Synonym synonym) {
        this.id = synonym.getSynonymID().getSynonymId();
        this.synonym = synonym.getSynonym().getWord();
        this.isSynonym = synonym.getIsSynonym();
    }

    static public SynonymDTO of(Synonym synonym) {
        return new SynonymDTO(synonym);
    }

    public SynonymDTO(Long id, String synonym, Boolean isSynonym) {
        this.id = id;
        this.synonym = synonym;
        this.isSynonym = isSynonym;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
