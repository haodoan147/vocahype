package com.vocahype.dto;

import lombok.Getter;

@Getter
public class WordDTO {
    private Long wordId;
    private Boolean status;
    private String word;

    public WordDTO(Long wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    @Override
    public String toString() {
        return wordId.toString();
    }
}
