package com.vocahype.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class TopicDTO {
    private Long id;
    private String name;
    private String description;
    private String emoji;
    private Long wordCount;
    private Long learningWordCount;
    private Long masteredWordCount;

    @Override
    public String toString() {
        return id.toString();
    }
}
