package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class WordUserKnowledgeDTO {
    private Long id;
    private Long wordId;
    private Boolean status;
    private String word;
    private Long count;
    private Double point;

    public WordUserKnowledgeDTO(Long wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    @Override
    public String toString() {
        return id == null ? wordId != null ? wordId.toString() : word : id.toString();
    }
}
