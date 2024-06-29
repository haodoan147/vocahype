package com.vocahype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.SingleSelectQuiz;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.dto.enumeration.LevelOfQuiz;
import com.vocahype.dto.enumeration.TypeOfQuiz;
import com.vocahype.exception.InvalidException;
import com.vocahype.exception.NoContentException;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.util.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AIService {

    private final ObjectMapper objectMapper;
    private final WebClient openAI;
    private final UserWordComprehensionRepository userWordComprehensionRepository;

    public AIService(final ObjectMapper objectMapper, final @Qualifier("openAI") WebClient openAI,
                     final UserWordComprehensionRepository userWordComprehensionRepository) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
        this.userWordComprehensionRepository = userWordComprehensionRepository;
    }

    public Map getQuizGen(final String word, final String level) throws JsonProcessingException {
        try {
            LevelOfQuiz.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Invalid level of quiz", "Invalid level: " + level);
        }
        SingleSelectQuiz singleSelectQuiz = SingleSelectQuiz.builder()
                .typeOfQuiz(TypeOfQuiz.WORD_USAGE_IN_CONTEXT)
                .levelOfQuiz(LevelOfQuiz.valueOf(level.toUpperCase()))
                .word(word)
                .question("What is an example sentence for the word?")
                .build();
        String userMessageContent = "{\"difficulty\":\"" + singleSelectQuiz.getLevelOfQuiz().getDetail() + "\", \"word\":\"" + singleSelectQuiz.getWord() + "\"}";
        String systemMessageContent = "You are a helpful assistant that smartly generates single-choice quizz based on English words and difficulty I provided. Provide your answer in JSON structure like this {\"question\":\"<Auto generate the question title of the quiz>\",\"options\":[\"<option 1>\",\"<option 2>\",\"<option 3>\",\"<option 4>\"],\"answer\":\"<the index number of the correct answer (0-based index)>\"}. Each option is an answer based on the quiz type you choose with the chosen difficulty to challenge the user without indicating A, B, C, D. There are only one answer is right, another answer is obviously wrong (obviously or not based on the difficulty of the question).";
        return generate(userMessageContent, systemMessageContent);
    }

    public Map getStory(final long days) throws JsonProcessingException {
        Set<String> word = getListWordStory(days);
        String userMessageContent = "{\"words\": " + word + "}";
        String systemMessageContent = "You are a helpful assistant that generates a short story from 200 to 300 word, based on English words in a Vocabulary learning app. Provide your answer in JSON structure like this {\"story\":\"<Auto generate the story based on the words>\"}.";

        return generate(userMessageContent, systemMessageContent);
    }

    public Set<String> getListWordStory(final long days) {
        Set<String> word = userWordComprehensionRepository.findByUserWordComprehensionID_UserIdAndUpdateAtAfterAndWordComprehensionLevelNotIn(
                        SecurityUtil.getCurrentUserId(),
                        Timestamp.valueOf(LocalDateTime.now().minusDays(days).truncatedTo(ChronoUnit.DAYS)),
                        List.of(Level.LEVEL_11.getLevel(), Level.LEVEL_12.getLevel())).stream()
                .map(userWordComprehension -> userWordComprehension.getWord().getWord()).collect(Collectors.toSet());
        if (word.isEmpty()) {
            throw new NoContentException("No word found", "User did not learn any word in the last " + days + " days");
        }
        return word;
    }

    private Map generate(final String userMessageContent, final String systemMessageContent)
            throws JsonProcessingException {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", systemMessageContent
        ));
        messages.add(Map.of(
                "role", "user",
                "content", userMessageContent
        ));
        String messagesJson = objectMapper.writeValueAsString(messages);
        String model = "gpt-3.5-turbo-0125";
        String responseBody = openAI.post()
                .body(BodyInserters.fromValue("{\"model\": \"" + model + "\", \"response_format\": {\"type\": \"json_object\"}, \"messages\":" + messagesJson + "}"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = new JSONObject(responseBody);

        JSONArray choicesArray = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choicesArray.getJSONObject(0);
        JSONObject messageObject = firstChoice.getJSONObject("message");
        String contentValue = messageObject.getString("content");
        ObjectMapper objectMapper = new ObjectMapper();
        Map contentObject = objectMapper.readValue(contentValue, Map.class);
        return contentObject;
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
}
