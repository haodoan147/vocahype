package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SynonymDTO {
    private Long id;
    private String synonym;
    private Boolean isSynonym;

    @Override
    public String toString() {
        return id.toString();
    }
}
