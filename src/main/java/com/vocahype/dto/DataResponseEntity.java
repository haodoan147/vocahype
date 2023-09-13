package com.vocahype.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DataResponseEntity {
    private String type;
    private String id;
    private Object attributes;
    private Map<String, Object> relationships;

    public DataResponseEntity(Object entity, Map<String, Object> relationships) {
        this(entity, relationships, false);
    }

    public DataResponseEntity(Object entity, Map<String, Object> relationships, final boolean isRelationship) {
        if (entity != null) {
            this.id = entity.toString();
            this.type = entity.getClass().getSimpleName().split("\\$")[0].toLowerCase();
            if (this.type.endsWith("dto")) this.type = this.type.substring(0, this.type.length() - 3);
        } else {
            this.id = "null";
            this.type = "null";
        }
        if (!isRelationship) {
            this.attributes = entity;
            this.relationships = relationships;
        }
    }
}
