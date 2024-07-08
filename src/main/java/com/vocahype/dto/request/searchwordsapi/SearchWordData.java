package com.vocahype.dto.request.searchwordsapi;

import lombok.Data;

import java.util.List;

@Data
public class SearchWordData {
    private List<Result> results;
}
