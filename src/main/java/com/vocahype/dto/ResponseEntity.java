package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResponseEntity {
    private DataResponseEntity data;

    public static ResponseEntity of(Object entity) {
        return new ResponseEntity(new DataResponseEntity(entity, Map.of()));
    }

    public static ResponseEntity of(Object entity, Map<String, Object> relationships) {
        return new ResponseEntity(new DataResponseEntity(entity, relationships));
    }
}
