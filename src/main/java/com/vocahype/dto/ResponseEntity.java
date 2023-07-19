package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseEntity {
    private DataResponseEntity data;

    public static ResponseEntity of(Object entity) {
        return new ResponseEntity(new DataResponseEntity(entity, List.of()));
    }

    public static ResponseEntity of(Object entity, List<Object> relationships) {
        return new ResponseEntity(new DataResponseEntity(entity, relationships));
    }
}
