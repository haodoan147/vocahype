package com.vocahype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vocahype.dto.enumeration.Level;
import com.vocahype.exception.InvalidException;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class ComprehensionDTO {
    private final String status;
    private final Date dueDate;
    private final Integer level;
    @JsonIgnore
    private final Long wordId;

    public ComprehensionDTO(Date dueDate, Integer level, Long wordId) {
        try {
            this.status = level == null ? "to learn" : Level.valueOf("LEVEL_" + level).getTitle();
        } catch (IllegalArgumentException e) {
            throw new InvalidException("Level must be between 1 and 12", "Level in database is invalid, level = " + level);
        }
        this.dueDate = dueDate;
        this.level = level;
        this.wordId = wordId;
    }

    @Override
    public String toString() {
        return wordId.toString();
    }
}
