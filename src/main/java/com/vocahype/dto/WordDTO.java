package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class WordDTO {
    private Long id;
    private Long wordId;
    private Boolean status;
    private String word;
    private Long count;
    private Double point;

    public WordDTO(Long wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    @Override
    public String toString() {
        return id == null ? wordId.toString() : id.toString();
    }
}
