package com.vocahype.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DefinitionDTO {
    private String definition;
    private List<String> examples;

    @Override
    public String toString() {
        return definition;
    }
}
