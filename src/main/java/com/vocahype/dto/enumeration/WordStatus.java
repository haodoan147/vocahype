package com.vocahype.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum WordStatus {
    LEARNING(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10)),
    TO_LEARN(null),
    MASTERED(List.of(11)),
    IGNORE(List.of(12));

    private final List<Integer> levelList;
    
}
