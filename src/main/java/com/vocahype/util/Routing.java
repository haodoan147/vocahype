package com.vocahype.util;

public class Routing {
    public static final String API_PUB = "/pub";
    public static final String API_BASE = "/api";
    public static final String USER = "/user";
    public static final String WORD= API_BASE + "/words";
    public static final String WORD_ID = WORD + "/{wordId}";
    public static final String WORD_USER_KNOWLEDGE = API_BASE + "/word-user-knowledge";
    public static final String KNOWLEDGE_TEST_50 = WORD + "/knowledge-test";
    public static final String SYNONYM = API_BASE + "/synonym";
    public static final String LEARNING_EASY = WORD_ID + "/easy";
    public static final String LEARNING_HARD = WORD_ID + "/hard";
    public static final String LEARNING_NORMAL = WORD_ID + "/normal";
    public static final String LEARNING_MASTERED = WORD_ID + "/mastered";
    public static final String LEARNING_IGNORE = WORD_ID + "/ignore";
    public static final String WORDS_LEARN = WORD + "/learn";
    public static final String WORDS_DELAY = WORD_ID + "/delay";
}
