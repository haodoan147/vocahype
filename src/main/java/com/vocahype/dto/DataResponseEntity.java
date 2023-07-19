package com.vocahype.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataResponseEntity {
    private String type;
    private String id;
    private Object attributes;
    private List<Object> relationships;

    public DataResponseEntity(Object entity, List<Object> relationships) {
        this.type = entity.getClass().getSimpleName();
        this.id = entity.toString();
        this.attributes = entity;
        this.relationships = relationships;
    }
}
