package com.vocahype.dto.enumeration;

public enum LearningGoal {

    basic(300),
    casual(600),
    regular(900),
    serious(1200),
    challenge(1500),
    hardcore(1800);

    private final int seconds;

    LearningGoal(final int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

}
