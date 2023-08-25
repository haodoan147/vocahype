package com.vocahype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
public class ResponseEntityJsonApi {
    private DataResponseEntity data;
    private DataResponseEntity included;

    public ResponseEntityJsonApi (Object entity) {
        Map<String, Object> relationships = new HashMap<>();
        Map<String, Object> included = new HashMap<>();
        List<Class<?>> primitiveTypes = Arrays.asList(
                Integer.class, Long.class, Double.class, Float.class, String.class, Timestamp.class, Boolean.class
        );
        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    if (!primitiveTypes.contains(value.getClass())) {
                        included.put(field.getName(), value);
                        relationships.put(field.getName(), Map.of("data", Map.of("type", value.getClass().getSimpleName().split("\\$")[0], "id", value.toString())));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        this.data = new DataResponseEntity(entity, relationships);
        this.included = new DataResponseEntity(included, Map.of());
    }

//    public static <K, V> Map<K, V> mapOfNullable(Object... keyValuePairs) {
//        if (keyValuePairs.length % 2 != 0) {
//            throw new IllegalArgumentException("Key-value pairs must be provided in even number.");
//        }
//
//        Map<K, V> map = new HashMap<>();
//        for (int i = 0; i < keyValuePairs.length; i += 2) {
//            V value = (V) keyValuePairs[i + 1];
//            if (value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty())) continue;
//            K key = (K) keyValuePairs[i];
//            map.put(key, (V) of(value));
//        }
//        return map;
//    }

    @Override
    public String toString() {
        return data.getAttributes().toString();
    }
}
