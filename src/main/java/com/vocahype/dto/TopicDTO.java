package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TopicDTO {
    private Long id;
    private String name;
    private String description;
    private String emoji;
    private Long wordCount;
    private Long learningWordCount;
    private Long masteredWordCount;
    private List<String> addedWordIds;
    private List<String> removedWordIds;
    @JsonIgnore
    private Set<WordDTO> wordInTopic;

    public TopicDTO(Long id, String name, String description, String emoji, Long wordCount, Long learningWordCount, Long masteredWordCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.wordCount = wordCount;
        this.learningWordCount = learningWordCount;
        this.masteredWordCount = masteredWordCount;
    }


    @Override
    public String toString() {
        return id.toString();
    }
}
