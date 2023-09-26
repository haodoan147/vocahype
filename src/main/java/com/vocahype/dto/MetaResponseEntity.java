package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MetaResponseEntity {
    Pagination pagination;
    public MetaResponseEntity(long first, long last, long currentPage, long size, long total) {
        this.pagination = new Pagination(first, last, currentPage, size, total);
    }
}

@Getter
@AllArgsConstructor
class Pagination {
    Long first;
    Long last;
    Long page;
    Long size;
    Long total;
}