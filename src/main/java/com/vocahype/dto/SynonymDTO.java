package com.vocahype.dto;

import com.vocahype.entity.Synonym;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SynonymDTO {
    private Long id;
    private String synonym;
    private Boolean isSynonym;

    public SynonymDTO(Synonym synonym) {
        this.id = synonym.getSynonymID().getSynonymId();
        this.synonym = synonym.getSynonym().getWord();
        this.isSynonym = synonym.getIsSynonym();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
