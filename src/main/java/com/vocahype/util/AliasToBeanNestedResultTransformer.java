package com.vocahype.util;

import lombok.extern.log4j.Log4j2;
import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.openapitools.jackson.nullable.JsonNullable;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Help to transform alises with nested alises
 */
@Log4j2
public class AliasToBeanNestedResultTransformer extends AliasedTupleSubsetResultTransformer {
    private static final PropertyAccessStrategyChainedImpl PROPERTY_ACCESS_STRATEGY = new PropertyAccessStrategyChainedImpl(
            PropertyAccessStrategyBasicImpl.INSTANCE,
            PropertyAccessStrategyFieldImpl.INSTANCE,
            PropertyAccessStrategyMapImpl.INSTANCE);
    private boolean initialized;
    private Class<?> resultClass;
    private Map<String, Class<?>> clazzMap = new HashMap<>();
    private Map<String, Setter> settersMap = new HashMap<>();

    public AliasToBeanNestedResultTransformer(final Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    public boolean isTransformedValueATupleElement(final String[] aliases, final int tupleLength) {
        return false;
    }

    @Override
    public Object transformTuple(final Object[] tuples, final String[] aliases) {

        Map<String, Object> nestedObjectsMap = new HashMap<>();

        Object result = null;
        try {
            result = resultClass.getDeclaredConstructor().newInstance();

            if (!initialized) {
                initialized = true;
                initialize(aliases);
            }

            for (int a = 0; a < aliases.length; a++) {

                String alias = aliases[a];
                Object tuple = tuples[a];

                Object baseObject = result;

                int index = alias.lastIndexOf(".");
                if (index > 0) {
                    String basePath = alias.substring(0, index);
                    baseObject = nestedObjectsMap.get(basePath);
                    if (baseObject == null) {
                        baseObject = clazzMap.get(basePath).getDeclaredConstructor().newInstance();
                        nestedObjectsMap.put(basePath, baseObject);
                    }
                }

                Setter setter = settersMap.get(alias);
                setValue(setter, baseObject, tuple);
            }

            for (Map.Entry<String, Object> entry : nestedObjectsMap.entrySet()) {
                Setter setter = settersMap.get(entry.getKey());
                if (entry.getKey().contains(".")) {

                    int index = entry.getKey().lastIndexOf(".");
                    String basePath = entry.getKey().substring(0, index);
                    Object obj = nestedObjectsMap.get(basePath);

                    setValue(setter, obj, entry.getValue());
                } else {
                    setValue(setter, result, entry.getValue());
                }
            }

        } catch (InstantiationException | IllegalAccessException e) {
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void setValue(final Setter setter, final Object target, final Object originalValue) {
        Object value = originalValue;
        Class<?> clazz = setter.getMethod().getParameterTypes()[0];
        if (Number.class.isAssignableFrom(clazz) && value == null) {
            if (Double.class.equals(clazz)) {
                value = 0D;
            } else if (Integer.class.equals(clazz)) {
                value = 0;
            } else if (Long.class.equals(clazz)) {
                value = 0L;
            }
        }

        if (JsonNullable.class.isAssignableFrom(clazz)) {
            if (value instanceof BigDecimal) {
                value = JsonNullable.of(((BigDecimal) value).doubleValue());
            } else {
                value = JsonNullable.of(value);
            }
        }

        if (value instanceof BigDecimal) {
            if (Long.class.equals(clazz)) {
                value = ((BigDecimal) value).longValue();
            } else if (Double.class.equals(clazz)) {
                value = ((BigDecimal) value).doubleValue();
            } else if (Integer.class.equals(clazz)) {
                value = ((BigDecimal) value).intValue();
            }
        }

        if (value instanceof Timestamp && Instant.class.isAssignableFrom(clazz)) {
            value = ((Timestamp) value).toInstant();
        }

        setter.set(target, value, null);
    }

    private Setter getSetter(final Class resultClass, final String fieldName) {
        return PROPERTY_ACCESS_STRATEGY.buildPropertyAccess(resultClass, fieldName).getSetter();
    }

    private Getter getGetter(final Class resultClass, final String fieldName) {
        return PROPERTY_ACCESS_STRATEGY.buildPropertyAccess(resultClass, fieldName).getGetter();
    }


    private void initialize(final String[] aliases) {

        for (int a = 0; a < aliases.length; a++) {

            String alias = aliases[a];

            Class<?> baseClass = resultClass;

            if (alias.contains(".")) {

                String[] split = alias.split("\\.");

                StringBuffer res = new StringBuffer();

                for (int i = 0; i < split.length; i++) {

                    if (res.length() > 0) res.append(".");

                    String item = split[i];
                    res.append(item);

                    String resString = res.toString();

                    if (i == split.length - 1) {
                        clazzMap.put(resString, baseClass);
                        settersMap.put(resString, getSetter(baseClass, item));
                        break;
                    }

                    Class<?> clazz = clazzMap.get(resString);
                    if (clazz == null) {
                        clazz = getGetter(baseClass, item).getReturnType();
                        settersMap.put(resString, getSetter(baseClass, item));
                        clazzMap.put(resString, clazz);
                    }
                    baseClass = clazz;
                }
            } else {
                clazzMap.put(alias, resultClass);
                settersMap.put(alias, getSetter(resultClass, alias));
            }

        }

    }
}

