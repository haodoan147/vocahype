package com.vocahype.dto;

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

    @Override
    public String toString() {
        return id.toString();
    }
}
