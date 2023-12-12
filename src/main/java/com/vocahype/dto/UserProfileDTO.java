package com.vocahype.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserProfileDTO {
    @NotNull
    @NotBlank
    private String level;
}
