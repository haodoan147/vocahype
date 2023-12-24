package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Definition;
import com.vocahype.entity.Example;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    public DefinitionDTO(Definition definition) {
        this.id = definition.getId();
        this.definition = definition.getDefinition();
        examples = new HashSet<>();
        definition.getExamples().forEach(example -> this.examples.add(new ExampleDTO(example)));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
