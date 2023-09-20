package com.vocahype.dto;

import com.vocahype.entity.Example;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExampleDTO {
    private Long id;
    private String example;

    public ExampleDTO(Example example) {
        this.id = example.getId();
        this.example = example.getExample();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
