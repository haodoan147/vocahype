package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionDTO {
    private Long id;
    private String definition;
    @JsonIgnore
    private Set<ExampleDTO> examples;

    @Override
    public String toString() {
        return id.toString();
    }
}
