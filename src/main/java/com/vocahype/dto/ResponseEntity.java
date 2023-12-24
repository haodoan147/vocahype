package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
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

    public static <K, V> Map<K, V> mapOfNullable(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be provided in even number.");
        }

        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            V value = (V) keyValuePairs[i + 1];
            if (value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty())) continue;
            K key = (K) keyValuePairs[i];
            map.put(key, (V) of(value));
        }
        return map;
    }

    @Override
    public String toString() {
        return data.getAttributes().toString();
    }
}
