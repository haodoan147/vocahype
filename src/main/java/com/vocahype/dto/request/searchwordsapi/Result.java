package com.vocahype.dto.request.searchwordsapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class Result {
    private Integer total;
    private Integer limit;
    private Integer page;
    private List<String> data;

}
