package com.vocahype.dto.request.wordsapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.dto.ComprehensionDTO;
import com.vocahype.dto.WordDTO;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class WordData {
    private String word;
    private List<Result> results;
    private Syllables syllables;
    private Pronunciation pronunciation;
    private double frequency;
    private ComprehensionDTO comprehension;

    public WordDTO toWordDTO() {
        WordDTO wordDTO = new WordDTO();
        wordDTO.setWord(this.word);
        wordDTO.setMeanings(this.results.stream().map(Result::toMeaningDTO).collect(java.util.stream.Collectors.toSet()));
        wordDTO.setSyllable(this.syllables.getCount());
        wordDTO.setPhonetic(this.pronunciation.getAll());
        wordDTO.setPoint(frequency);
        return wordDTO;
    }
}
