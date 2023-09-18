package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.entity.Definition;
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
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExampleDTO {
    private Long id;
    private String example;

    @Override
    public String toString() {
        return id.toString();
    }
}
