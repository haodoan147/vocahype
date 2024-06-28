package com.vocahype.util;

public class Routing {
    public static final String API_PUB = "/pub";
    public static final String API_BASE = "/api";
    public static final String PROFILE = API_BASE + "/profile";
    public static final String WORD = API_BASE + "/words";
    public static final String WORD_STORY = API_BASE + "/story";
    public static final String LIST_WORD_STORY = WORD_STORY + "/list-word";
    public static final String WORD_QUIZ = WORD + "/quiz";
    public static final String WORD_ID = WORD + "/{wordId}";
    public static final String WORD_USER_KNOWLEDGE = API_BASE + "/word-user-knowledge";
    public static final String KNOWLEDGE_TEST_50 = WORD + "/knowledge-test";
    public static final String LEARNING_EASY = WORD_ID + "/easy";
    public static final String LEARNING_HARD = WORD_ID + "/hard";
    public static final String LEARNING_NORMAL = WORD_ID + "/normal";
    public static final String LEARNING_MASTERED = WORD_ID + "/mastered";
    public static final String LEARNING_IGNORE = WORD_ID + "/ignore";
    public static final String WORDS_LEARN = WORD + "/learn";
    public static final String WORDS_DELAY = WORD_ID + "/delay";
    public static final String LEARNING_TIME = API_BASE + "/learning-time";
    public static final String RESET_LEARNING_PROGRESSION = API_BASE + "/reset-learning-progression";
    public static final String RESET_LEARNING_PROGRESSION_WORD_ID = RESET_LEARNING_PROGRESSION + "/word/{wordId}";
    public static final String DAILY_GOAL = PROFILE + "/daily-goal";
    public static final String PROFILE_TOPIC = PROFILE + "/topic";
    public static final String FETCH_DICTIONARY = API_BASE + "/fetch-dictionary";
    public static final String FETCH_DICTIONARY_WORD = FETCH_DICTIONARY + "/{word}";
    public static final String TOPICS = API_BASE + "/topics";
    public static final String TOPIC_ID = TOPICS + "/{topicId}";
    public static final String REPORT = API_BASE + "/report";
    public static final String IMPORT = API_BASE + "/import";
    public static final String USER = API_BASE + "/user";
    public static final String USER_ID = USER + "/{userId}";

}
