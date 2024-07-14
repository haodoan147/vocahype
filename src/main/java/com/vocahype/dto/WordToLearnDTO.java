package com.vocahype.dto;

import lombok.Data;

@Data
public class WordToLearnDTO {
    private String text;
    private String url;

    public WordToLearnDTO(String text) {
        this.text = Character.toUpperCase(text.charAt(0)) + text.substring(1);
        this.url = "https://vocahype.netlify.app/words/" + text;
    }
}
