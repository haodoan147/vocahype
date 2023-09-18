package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Example;
import com.vocahype.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
