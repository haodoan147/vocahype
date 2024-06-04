package com.vocahype.util;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import java.util.LinkedHashMap;
import java.util.Map;

public class AliasToEntityLinkHashMapResultTransformer extends AliasedTupleSubsetResultTransformer {
    public static final AliasToEntityLinkHashMapResultTransformer INSTANCE = new AliasToEntityLinkHashMapResultTransformer();

    @Override
    public boolean isTransformedValueATupleElement(final String[] aliases, final int tupleLength) {
        return false;
    }

    @Override
    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        Map<String, Object> result = new LinkedHashMap(tuple.length);
        for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                result.put(alias, tuple[i]);
            }
        }
        return result;
    }

}
