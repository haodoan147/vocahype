package com.vocahype.dto.enumeration;

public enum Assessment {
    EASY(1, "easy"),
    NORMAL(2, "normal"),
    HARD(3, "hard"),
    MASTERED(4, "mastered"),
    IGNORE(5, "ignore");

    private final long id;
    private final String title;

    Assessment(final long id, final String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }
}
