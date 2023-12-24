package com.vocahype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.SingleSelectQuiz;
import com.vocahype.dto.enumeration.LevelOfQuiz;
import com.vocahype.dto.enumeration.TypeOfQuiz;
import com.vocahype.dto.request.QuizRequest;
import com.vocahype.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final ObjectMapper objectMapper;

    public String getQuizGen(final QuizRequest quizRequest) throws JsonProcessingException {
        String apiKey = "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";

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

        Map<String, String> messages = Map.of(
                "system", systemMessageContent,
                "user", userMessageContent
        );

        // Convert the messages to JSON
        String messagesJson = objectMapper.writeValueAsString(messages);

        // Set up your Spring WebClient
        WebClient webClient = WebClient.builder().baseUrl("https://api.openai.com/v1/engines/gpt-3.5-turbo-1106/completions").defaultHeader("Authorization", "Bearer " + apiKey).build();

        // Make a request to OpenAI API using WebClient
        String responseFormat = "json_object";
        String responseBody = webClient.put()
                .uri("")
                .body(BodyInserters.fromValue("{\"messages\":" + messagesJson + ", \"response_format\": \"" + responseFormat + "\"}"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Parse and handle the response as needed
        return responseBody;
    }

}
