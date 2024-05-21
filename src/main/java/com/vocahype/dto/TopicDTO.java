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
    private List<Long> addedWordIds;
    private List<Long> removedWordIds;
    @JsonIgnore
    private Set<WordDTO> wordInTopic;

    @Override
    public String toString() {
        return id.toString();
    }
}
