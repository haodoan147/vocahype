package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.exception.InvalidException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class FrequencyDTO {
    private String word;
    private Integer frequency;
    private BigInteger count;
    private String status;
    private Date dueDate;
    private Integer level;
    private Boolean isInTopic;

    public void setStatus() {
        try {
            this.status = level == null ? "to learn" : Level.valueOf("LEVEL_" + level).getTitle();
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Level must be between 1 and 12", "Level in database is invalid, level = " + level);
        }
    }

    public FrequencyDTO(String word, Integer frequency, BigInteger count, Date dueDate, Integer level) {
        this.word = word;
        this.frequency = frequency;
        this.count = count;
        try {
            this.status = level == null ? "to learn" : Level.valueOf("LEVEL_" + level).getTitle();
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Level must be between 1 and 12", "Level in database is invalid, level = " + level);
        }
        this.dueDate = dueDate;
        this.level = level;
    }
}
