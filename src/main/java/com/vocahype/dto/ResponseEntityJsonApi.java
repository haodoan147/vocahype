package com.vocahype.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.collection.internal.PersistentSet;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
public class ResponseEntityJsonApi {
    public static final List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(
            Integer.class, Long.class, Double.class, Float.class, String.class, Timestamp.class, Boolean.class
    );
    private List<DataResponseEntity> data = new ArrayList<>();
    private List<DataResponseEntity> included = new ArrayList<>();

    public ResponseEntityJsonApi (Object entity) {
        Map<String, Object> relationships = new HashMap<>();
        Map<String, Object> included = new HashMap<>();
        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
                    if (value.getClass().equals(ArrayList.class)) {
                        ArrayList<?> list = ((ArrayList<?>) value);
                        relationships.put(field.getName(), Map.of("data", list.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
                        list.forEach(item -> {
                            this.included.add(new DataResponseEntity(item, null));
                        });

                } else if (value.getClass().equals(PersistentSet.class) && ((PersistentSet) value).size() > 0) {
                        PersistentSet set = ((PersistentSet) value);
                        relationships.put(field.getName(), Map.of("data", set.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
                        set.forEach(item -> {
                            if (!PRIMITIVE_TYPES.contains(item.getClass())) {
                                this.included.add(new DataResponseEntity(item, null));
                            }
                        });
                    } else if (!PRIMITIVE_TYPES.contains(value.getClass())) {
                            this.included.add(new DataResponseEntity(value, null));
//                        included.put(field.getName(), value);
//                        this.included.add(new DataResponseEntity(value, null));
//                        relationships.put(field.getName(), Map.of("data", Map.of("type", value.getClass().getSimpleName().split("\\$")[0], "id", value.toString())));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        data.add(new DataResponseEntity(entity, relationships));
//        this.included.add(new DataResponseEntity(included, Map.of()));
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

}
