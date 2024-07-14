package com.vocahype.dto;

import lombok.Data;

import java.util.List;

@Data
public class FrequencyResponseDTO {
    private Integer total;
    private Integer limit;
    private Integer page;
    private List<FrequencyDTO> data;

}
