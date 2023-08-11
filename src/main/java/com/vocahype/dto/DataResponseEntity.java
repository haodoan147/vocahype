package com.vocahype.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DataResponseEntity {
    private String id;
    private Object attributes;
    private Map<String, Object> relationships;

    public DataResponseEntity(Object entity, Map<String, Object> relationships) {
        if (entity != null) this.id = entity.toString();
        this.attributes = entity;
        this.relationships = relationships;
    }
}
