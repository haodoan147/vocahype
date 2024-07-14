package com.vocahype.dto.request.wordsapi;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class Syllables {
    private int count;
    private List<String> list;
}
