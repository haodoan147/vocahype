package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;

@Data
public class FrequencyDTO {
    private String lemma;
    private Integer frequency;
    private BigInteger count;
}
