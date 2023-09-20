package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MetaResponseEntity {
    Pagination pagination;
    public MetaResponseEntity(int first, int last, int currentPage, int size, int total) {
        this.pagination = new Pagination(first, last, currentPage == first ? null : currentPage - 1,
                currentPage == last ? null : currentPage + 1, currentPage, size, total);
    }
}

@Getter
@AllArgsConstructor
class Pagination {
    Integer first;
    Integer last;
    Integer prev;
    Integer next;
    Integer page;
    Integer size;
    Integer total;
}