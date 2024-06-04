package com.vocahype.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    LEVEL_1(0, "to learn"),
    LEVEL_2(1, "learning"),
    LEVEL_3(3, "learning"),
    LEVEL_4(7, "learning"),
    LEVEL_5(14, "learning"),
    LEVEL_6(30, "learning"),
    LEVEL_7(60, "learning"),
    LEVEL_8(90, "learning"),
    LEVEL_9(180, "learning"),
    LEVEL_10(365, "learning"),
    LEVEL_11(-1, "mastered"), // null next learning
    LEVEL_12(-1, "ignore"); // null next learning

    private final long day;
    private final String title;
}
