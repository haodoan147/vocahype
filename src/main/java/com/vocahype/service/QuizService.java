package com.vocahype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.configuration.ApplicationProperties;
import com.vocahype.dto.SingleSelectQuiz;
import com.vocahype.dto.enumeration.LevelOfQuiz;
import com.vocahype.dto.enumeration.TypeOfQuiz;
import com.vocahype.dto.request.QuizRequest;
import com.vocahype.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;

    public Map getQuizGen(final QuizRequest quizRequest) throws JsonProcessingException {
        String apiKey = new String(Base64.getDecoder().decode(applicationProperties.getOpenAiApiKey()));

        // Create a single select quiz
        try {
            LevelOfQuiz.valueOf(quizRequest.getLevel().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Invalid level of quiz", "Invalid level: " + quizRequest.getLevel());
        }
        SingleSelectQuiz singleSelectQuiz = SingleSelectQuiz.builder()
                .typeOfQuiz(TypeOfQuiz.WORD_USAGE_IN_CONTEXT)
                .levelOfQuiz(LevelOfQuiz.valueOf(quizRequest.getLevel().toUpperCase()))
                .word(quizRequest.getWord())
                .question("What is an example sentence for the word?")
                .build();

        // Create JSON content for the user message
        String userMessageContent = "{\"question\":\"follow template here: " + singleSelectQuiz.getQuestion() + "\", \"difficulty_of_answer\":\"" + singleSelectQuiz.getLevelOfQuiz().getDetail() + "\", \"correctAnswer\":\"" + singleSelectQuiz.getWord() + "\", \"typeOfQuiz\":\"" + singleSelectQuiz.getTypeOfQuiz().getDetail() + "\"}";

        // Create a chat message for OpenAI API
        String systemMessageContent = "You are a helpful assistant that generates quizzes based on English words in a Vocabulary learning app. Provide your answer in JSON structure like this {\"question\":\"<Auto generate the question title of the quiz>\",\"options\":[\"<option 1>\",\"<option 2>\",\"<option 3>\",\"<option 4>\"],\"answer\":\"<the index number of the correct answer>\"}. Each option is an answer based on the quiz type with the chosen difficulty to challenge the user without indicating A, B, C, D.";

        List<Map<String, String>> messages = new ArrayList<>();
//        messages.add(Map.of(
//                "system", systemMessageContent,
//                "user", userMessageContent
//        ));

        messages.add(Map.of(
                "role", "system",
                "content", systemMessageContent
        ));


        messages.add(Map.of(
                "role", "user",
                "content", userMessageContent
        ));
        // Convert the messages to JSON
        String messagesJson = objectMapper.writeValueAsString(messages);

        // Set up your Spring WebClient
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json").build();

        // Make a request to OpenAI API using WebClient
        String responseFormat = "json_object";
        String model = "gpt-3.5-turbo-1106";
        String responseBody = webClient.post()
                .body(BodyInserters.fromValue("{\"model\": \"" + model + "\", \"messages\":" + messagesJson + "}"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = new JSONObject(responseBody);

        // Get the "choices" array
        JSONArray choicesArray = jsonObject.getJSONArray("choices");

        // Get the first element from the "choices" array
        JSONObject firstChoice = choicesArray.getJSONObject(0);

        // Get the "message" object from the first choice
        JSONObject messageObject = firstChoice.getJSONObject("message");

        // Get the value of the "content" field from the "message" object
        String contentValue = messageObject.getString("content");
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON content into the ContentObject
        Map contentObject = objectMapper.readValue(contentValue, Map.class);
        // Parse and handle the response as needed
        return contentObject;
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
}
