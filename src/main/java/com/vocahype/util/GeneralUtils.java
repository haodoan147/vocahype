package com.vocahype.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocahype.dto.TopicDTO;
import com.vocahype.dto.WordDTO;
import com.vocahype.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GeneralUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static TopicDTO convertToDto(Topic topic) {
        return convertToDto(topic, false);
    }

    public static TopicDTO convertToDto(Topic topic, boolean includeWordTopics) {
        TopicDTO topicDTO = modelMapper.map(topic, TopicDTO.class);
        if (topic.getWordTopics() != null) {
            topicDTO.setWordCount((long) topic.getWordTopics().size());
            if (includeWordTopics) {
                topicDTO.setWordInTopic(topic.getWordTopics().stream().map(wordTopic -> WordDTO.builder().word(wordTopic.getWordTopicID().getWord())
                        .word(wordTopic.getWordTopicID().getWord()).build()).collect(Collectors.toSet()));
            }
        }
        return topicDTO;
    }

    public static Map<String, Integer> generateDateMap(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> dateMap = new HashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dateMap.put(currentDate.toString(), 0);
            currentDate = currentDate.plusDays(1);
        }
        return dateMap;
    }

    public static void sendFailAuthenticationBody(HttpServletResponse response, String message, Exception e) throws IOException {
        String detail = e.getMessage();

        Map<String, Object> error = new HashMap<>();
        error.put("title", message);
        error.put("detail", detail);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errors", error);

        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    public static Collection<GrantedAuthority> convert(final String roles) {
        return roles != null
                ? StringUtils.commaDelimitedListToSet(roles)
                .stream()
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static String removeNonAlphanumericSuffix(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Regular expression to match any character that is not alphanumeric
        String regex = "[^a-zA-Z0-9]$";

        // Loop to remove non-alphanumeric characters from the end
        while (input.matches(".*" + regex)) {
            input = input.substring(0, input.length() - 1);
        }

        return input;
    }
}
