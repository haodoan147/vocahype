package com.vocahype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.FrequencyDTO;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.dto.quiz.DefinitionSelectAnswer;
import com.vocahype.dto.quiz.QuizDTO;
import com.vocahype.dto.quiz.QuizType;
import com.vocahype.dto.request.wordsapi.Result;
import com.vocahype.dto.request.wordsapi.WordData;
import com.vocahype.exception.InvalidException;
import com.vocahype.exception.NoContentException;
import com.vocahype.repository.UserWordComprehensionRepository;
import com.vocahype.util.GeneralUtils;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AIService {

    private final ObjectMapper objectMapper;
    private final WebClient openAI;
    private final UserWordComprehensionRepository userWordComprehensionRepository;
    private final WordService wordService;

    public AIService(final ObjectMapper objectMapper, final @Qualifier("openAI") WebClient openAI,
                     final UserWordComprehensionRepository userWordComprehensionRepository,
                     final WordService wordService) {
        this.objectMapper = objectMapper;
        this.openAI = openAI;
        this.userWordComprehensionRepository = userWordComprehensionRepository;
        this.wordService = wordService;
    }

    public QuizDTO getQuizGen(final String word) {
//        try {
//            LevelOfQuiz.valueOf(level.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidException("Invalid level of quiz", "Invalid level: " + level);
//        }
        QuizType quizType = QuizType.getRandomTypeSingle();
        try {
            switch (quizType) {
                case DEFINITION_SINGLE_SELECT:
                    return getDefinitionSingleSelect(word);
                case DEFINITION_MULTIPLE_SELECT:
                    return getDefinitionMultipleSelect(word);
                case TRUE_FALSE:
                    return getTrueFalse(word);
                case RELATED_WORD_SELECT:
                    return getRelatedWordSelect(word);
                default:
                    throw new InvalidException("Invalid quiz type", "Invalid quiz type: " + quizType);
            }
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private QuizDTO getWordGuess(String word) {
        WordData wordData = wordService.getWordData(word).block();
        if (wordData == null) {
            return getQuizGen(word);
        }
        List<Result> result = GeneralUtils.shuffleList(wordData.getResults(), 1);
        if (result == null || result.isEmpty()) {
            return getQuizGen(word);
        }
        Result quizResult = result.get(0);
        String description = "The word is a " + quizResult.getPartOfSpeech() + ". "
                + "It has " + wordData.getSyllables().getCount() + " syllables. "
                + (quizResult.getSynonyms() == null ? "" : "The synonym of the word is " + GeneralUtils.listToSense(quizResult.getSynonyms()) + ". ")
                + (quizResult.getAntonyms() == null ? "" : "The antonym of the word is " + GeneralUtils.listToSense(quizResult.getAntonyms()) + ". ")
                + (quizResult.getSimilarTo() == null ? "" : "The word is similar to " + GeneralUtils.listToSense(quizResult.getSimilarTo()) + ". ");
        return QuizDTO.builder()
                .type(QuizType.WORD_GUESS.getTitle())
                .question("Guess the original word using the following information.")
                .word(word)
                .description(description)
                .build();
    }

    private QuizDTO getAntonymSynonymMatch(String word) {
        WordData list = wordService.getWordData(word).block();
        if (list == null) {
            return getQuizGen(word);
        }
        List<Result> resultList = list.getResults().stream().filter(result -> result.getSynonyms() != null
                || result.getAntonyms() != null).collect(Collectors.toList());
        List<String> quizSynonymsResult = new ArrayList<>();
        List<String> quizAntonymsResult = new ArrayList<>();
        GeneralUtils.shuffleList(resultList, resultList.size()).forEach(result -> {
            if (result.getSynonyms() != null) {
                quizSynonymsResult.addAll(result.getSynonyms());
            }
            if (result.getAntonyms() != null) {
                quizAntonymsResult.addAll(result.getAntonyms());
            }
        });
        int minAntonyms = Math.min(quizAntonymsResult.size(), 2);
        int minSynonyms = 3 - minAntonyms;
        List<Map<String, Object>> antonymSynonym = new ArrayList<>();
        GeneralUtils.shuffleList(quizAntonymsResult, minAntonyms).forEach(s ->
                antonymSynonym.add(Map.of("text", s, "isAntonym", true)));
        GeneralUtils.shuffleList(quizSynonymsResult, minSynonyms).forEach(s ->
                antonymSynonym.add(Map.of("text", s, "isSynonym", true)));
        quizSynonymsResult.addAll(quizAntonymsResult);
        userWordComprehensionRepository.getRandomWordsNotIn(5, quizSynonymsResult).forEach(frequencyDTO ->
                antonymSynonym.add(Map.of("text", frequencyDTO.getWord(), "isAntonym", false, "isSynonym", false)));
        return QuizDTO.builder()
                .type(QuizType.ANTONYM_SYNONYM_MATCH.getTitle())
                .question("Match the antonym and synonym of the word.")
                .result(GeneralUtils.shuffleList(antonymSynonym, 8))
                .word(word)
                .build();
    }

    private QuizDTO getRelatedWordSelect(String word) {
        WordData list = wordService.getWordData(word).block();
        if (list == null) {
            return getQuizGen(word);
        }
        List<Result> resultList = list.getResults().stream().filter(result -> result.getSynonyms() != null
                || result.getAntonyms() != null || result.getSimilarTo() != null).collect(Collectors.toList());
        List<String> quizResult = new ArrayList<>();;
        resultList.forEach(result -> {
            if (result.getSimilarTo() != null) {
                quizResult.addAll(result.getSimilarTo());
            }
            if (result.getSynonyms() != null) {
                quizResult.addAll(result.getSynonyms());
            }
            if (result.getAntonyms() != null) {
                quizResult.addAll(result.getAntonyms());
            }
        });
        List<Map<String, Object>> relatedWords = new ArrayList<>();
        int min = Math.min(resultList.size(), 3);
        GeneralUtils.shuffleList(resultList, min).forEach(result -> {
            if (result.getSimilarTo() != null) {
                relatedWords.add(Map.of("text", GeneralUtils.shuffleList(result.getSimilarTo(), 1).get(0), "correct", true, "type", "similar", "definition", result.getDefinition()));
            }
            if (result.getSynonyms() != null) {
                relatedWords.add(Map.of("text", GeneralUtils.shuffleList(result.getSynonyms(), 1).get(0), "correct", true, "type", "synonym", "definition", result.getDefinition()));
            }
            if (result.getAntonyms() != null) {
                relatedWords.add(Map.of("text", GeneralUtils.shuffleList(result.getAntonyms(), 1).get(0), "correct", true, "type", "antonym", "definition", result.getDefinition()));
            }
        });
        userWordComprehensionRepository.getRandomWordsNotIn(8 - min, quizResult).forEach(frequencyDTO ->
                relatedWords.add(Map.of("text", frequencyDTO.getWord(), "correct", false)));
        return QuizDTO.builder()
                .type(QuizType.RELATED_WORD_SELECT.getTitle())
                .question("Select the word that is related to the word.")
                .result(GeneralUtils.shuffleList(relatedWords, 8))
                .word(word)
                .build();
    }

    private QuizDTO getTrueFalse(String word) throws JsonProcessingException {
        WordData wordData = wordService.getWordData(word).block();
        if (wordData == null) {
            return getQuizGen(word);
        }
        wordData.setFrequency(null);
        String userMessageContent = "{\"word\":\"" + wordData.toString() + "\"}";
        String systemMessageContent = "You are a helpful assistant that smartly generates a yes or no quiz based on English words details. Provide your answer in JSON structure like this {\"question\":\"<Auto generate the question title of the quiz>\",\"answer\":\"<answer of question (true or false)>\"}. The question should be a clear right or wrong question, it should not be a question with an answer that causes confusion, and do not ask uncommon word's definition and do not ask about word's example. Example: {\"question\":\"The quality of being capable is called capability.\",\"answer\":\"true\"} for {\"word\":\"capability\"}";
        Map generate = generate(userMessageContent, systemMessageContent);
        return QuizDTO.builder()
                .type(QuizType.TRUE_FALSE.getTitle())
                .question("Is the following statement true or false?")
                .statement(generate.get("question").toString())
                .result(generate.get("answer").toString())
                .word(word)
                .build();
    }

    private QuizDTO getDefinitionMultipleSelect(String word) throws JsonProcessingException {
        List<String> randomWords = userWordComprehensionRepository.getRandomWords(4).stream().map(FrequencyDTO::getWord).collect(Collectors.toList());
        randomWords.add(word);
        List<WordData> list = wordService.getWordDataInParallel(randomWords);
        List<DefinitionSelectAnswer> results = new ArrayList<>();
        list.forEach(wordData -> {
            if (wordData.getWord().equals(word)) {
                results.addAll(0, GeneralUtils.shuffleList(wordData.getResults(), 8).stream().map(result ->
                        DefinitionSelectAnswer.builder().text(result.getDefinition()).correct(true).build()).collect(Collectors.toList()));
            }
            results.add(DefinitionSelectAnswer.builder().text(GeneralUtils.shuffleList(wordData.getResults(), 1).get(0).getDefinition()).correct(false).build());
        });
        return QuizDTO.builder()
                .type(QuizType.DEFINITION_MULTIPLE_SELECT.getTitle())
                .question("Select all the definitions that match the word.")
                .result(GeneralUtils.shuffleList(results, 8))
                .word(word)
                .build();
    }

    private QuizDTO getDefinitionSingleSelect(String word) throws JsonProcessingException, ExecutionException, InterruptedException {
        List<String> randomWords = userWordComprehensionRepository.getRandomWords(3).stream().map(FrequencyDTO::getWord).collect(Collectors.toList());
        randomWords.add(word);
        List<WordData> list = wordService.getWordDataInParallel(randomWords);
        list = GeneralUtils.shuffleList(list, 4);
        List<DefinitionSelectAnswer> selectAnswers = list.stream().map(wordData ->
                DefinitionSelectAnswer.builder().text(GeneralUtils.shuffleList(wordData.getResults(), 1)
                        .get(0).getDefinition()).correct(wordData.getWord().equals(word)).build()).collect(Collectors.toList());
        return QuizDTO.builder()
                .type(QuizType.DEFINITION_SINGLE_SELECT.getTitle())
                .question("Select a definition that match the word.")
                .result(selectAnswers)
                .word(word)
                .build();
    }

    private QuizDTO getWordScramble(String word) {
        String scrambledWord = scrambleWord(word);
        return QuizDTO.builder()
                .type(QuizType.WORD_SCRAMBLE.getTitle())
                .question("Re-arrange the letters to form a word.")
                .word(word)
                .result(scrambledWord)
                .build();
    }

    private String scrambleWord(String word) {
        List<Character> characters = Arrays.asList(word.chars().mapToObj(c -> (char) c).toArray(Character[]::new));
        Collections.shuffle(characters);
        StringBuilder scrambledWord = new StringBuilder();
        for (char c : characters) {
            scrambledWord.append(c);
        }
        return scrambledWord.toString();
    }

    public Map getStory(final long days) throws JsonProcessingException {
        Set<String> word = getListWordStory(days);
        String userMessageContent = "{\"words\": " + word + "}";
        String systemMessageContent = "You are a helpful assistant that generates a short story from 200 to 300 word, based on English words in a Vocabulary learning app. Provide your answer in JSON structure like this {\"story\":\"<Auto generate the story based on the words>\"}.";

        return generate(userMessageContent, systemMessageContent);
    }

    public Set<String> getListWordStory(final long days) {
        Set<String> word = userWordComprehensionRepository.findByUserWordComprehensionID_UserIdAndUpdateAtAfterAndWordComprehensionLevelNotInOrderByUpdateAtDesc(
                        SecurityUtil.getCurrentUserId(),
                        Timestamp.valueOf(LocalDateTime.now().minusDays(days).truncatedTo(ChronoUnit.DAYS)),
                        List.of(Level.LEVEL_11.getLevel(), Level.LEVEL_12.getLevel())).stream()
                .map(userWordComprehension -> userWordComprehension.getUserWordComprehensionID().getWord()).collect(Collectors.toSet());
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

    public QuizDTO getQuizGens(final String word) {
//        try {
//            LevelOfQuiz.valueOf(level.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidException("Invalid level of quiz", "Invalid level: " + level);
//        }
        QuizType quizType = QuizType.getRandomTypeMulti();
        try {
            switch (quizType) {
                case WORD_SCRAMBLE:
                    return getWordScramble(word);
                case DEFINITION_SINGLE_SELECT:
                    return getDefinitionSingleSelect(word);
                case DEFINITION_MULTIPLE_SELECT:
                    return getDefinitionMultipleSelect(word);
                case TRUE_FALSE:
                    return getTrueFalse(word);
                case RELATED_WORD_SELECT:
                    return getRelatedWordSelect(word);
                case WORD_GUESS:
                    return getWordGuess(word);
                default:
                    throw new InvalidException("Invalid quiz type", "Invalid quiz type: " + quizType);
            }
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<QuizDTO> getQuizGens(Integer days) {
        List<String> word = new ArrayList<>(getListWordStory(days));
        if (word.size() < 10) {
            Random random = new Random();
            while (word.size() < 10) {
                String randomElement = word.get(random.nextInt(word.size()));
                word.add(randomElement);
            }
        }
        return word.parallelStream()
                .map(w -> {
                    try {
                        return getQuizGens(w);
                    } catch (NullPointerException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
    }
}
