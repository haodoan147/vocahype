package com.vocahype.dto.request.hasCategories;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class HasCategories {
    private String word;
    private Set<String> hasCategories;
    private Boolean success;
    private String message;
}
