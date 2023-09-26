package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ComprehensionDTO {
    private String status;
    private Date dueDate;
    private Integer level;

    @Override
    public String toString() {
        return "ComprehensionDTO{" +
                "status='" + status + '\'' +
                ", dueDate=" + dueDate +
                ", level=" + level +
                '}';
    }
}
