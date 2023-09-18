package com.vocahype.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ResponseEntityJsonApi {
    public static final List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(
            Integer.class, Long.class, Double.class, Float.class, String.class, Timestamp.class, Boolean.class
    );
    private List<DataResponseEntity> data = new ArrayList<>();
    private List<DataResponseEntity> included = new ArrayList<>();

    @Async
    protected void add(Object entity) {
        Map<String, Object> relationships = new HashMap<>();
        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null) {
//                    if (value.getClass().equals(ArrayList.class) && ((ArrayList<?>) value).size() > 0) {
//                        ArrayList<?> list = ((ArrayList<?>) value);
//                        relationships.put(field.getName(), Map.of("data", list.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
//                        list.forEach(item -> {
////                            this.included.(entity2 -> entity2.getClass().getSimpleName()).collect(Collectors.toSet());
//                            if (!PRIMITIVE_TYPES.contains(item.getClass())) {
//                                ResponseEntityJsonApi jsonApi = new ResponseEntityJsonApi(item);
//                                this.included.addAll(jsonApi.data);
//                                this.included.addAll(jsonApi.included);
//                            }
//                        });
//                    } else if (value.getClass().equals(PersistentSet.class) && ((PersistentSet) value).size() > 0) {
//                        PersistentSet set = ((PersistentSet) value);
//                        relationships.put(field.getName(), Map.of("data", set.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
//                        set.forEach(item -> {
//                            if (!PRIMITIVE_TYPES.contains(item.getClass())) {
//                                ResponseEntityJsonApi jsonApi = new ResponseEntityJsonApi(item);
//                                this.included.addAll(jsonApi.data);
//                                this.included.addAll(jsonApi.included);
//                            }
//                        });
//                    } else if (value.getClass().equals(HashSet.class) && ((HashSet) value).size() > 0) {
//                        HashSet set = ((HashSet) value);
//                        relationships.put(field.getName(), Map.of("data", set.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
//                        set.forEach(item -> {
//                            if (!PRIMITIVE_TYPES.contains(item.getClass())) {
//                                ResponseEntityJsonApi jsonApi = new ResponseEntityJsonApi(item);
//                                this.included.addAll(jsonApi.data);
//                                this.included.addAll(jsonApi.included);
//                            }
//                        });
//                    } else
                        if (value instanceof Collection || value.getClass().equals(ArrayList.class)) {
                            Collection collection = ((Collection) value);
                            if (collection.size() > 0) {
                                List<DataResponseEntity> relation = new ArrayList<>();
//                                relationships.put(field.getName(), Map.of("data", collection.stream().map(item -> new DataResponseEntity(item, Map.of(), true)).toArray()));
                                collection.forEach(item -> {
//                            relationships.put(field.getName(), Map.of("data", new DataResponseEntity(item, Map.of(), true)));
//                            relationships.put(field.getName(), Map.of("data", Arrays.asList(collection.stream()
//                                    .map(item -> new DataResponseEntity(item, Map.of(), true))
//                                    .toArray())));
                                    if (!PRIMITIVE_TYPES.contains(item.getClass())) {
                                        relation.add(new DataResponseEntity(item, Map.of(), true));
                                        ResponseEntityJsonApi jsonApi = new ResponseEntityJsonApi(item);
                                        this.included.addAll(jsonApi.data);
                                        this.included.addAll(jsonApi.included);
                                    }
                                });
                                relationships.put(field.getName(), Map.of("data", relation));
                            }
                    } else if (!PRIMITIVE_TYPES.contains(value.getClass())) {
                        relationships.put(field.getName(), Map.of("data", new DataResponseEntity(value, Map.of(), true)));
//                        relationships.put(field.getName(), new ResponseEntityJsonApi(value));
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

//    private ResponseEntityJsonApi sort() {
//        this.data = this.data.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
//                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
//        this.included = this.included.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
//                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
//        return this;
//    }

    public ResponseEntityJsonApi (Object entity) {
        if (entity instanceof Collection || entity.getClass().equals(ArrayList.class)) ((Collection<?>) entity).forEach(this::add);
        else add(entity);
        this.data = this.data.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
        this.included = this.included.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
    }

    public ResponseEntityJsonApi (List<?> entity) {
        entity.forEach(this::add);
        this.data = this.data.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
        this.included = this.included.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
    }
//    public ResponseEntityJsonApi response(List<?> entities) {
//        ResponseEntityJsonApi responseEntityJsonApi = new ResponseEntityJsonApi();
//        responseEntityJsonApi.add(entities);
//        this.data = this.data.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
//                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
//        this.included = this.included.stream().distinct().sorted(Comparator.comparing(DataResponseEntity::getType)
//                .thenComparing(DataResponseEntity::getId)).collect(Collectors.toList());
//        return this;
//    }
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
