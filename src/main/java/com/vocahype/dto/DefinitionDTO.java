package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DefinitionDTO {
    private String definition;
    private List<String> examples;

    @Override
    public String toString() {
        return definition;
    }
}
