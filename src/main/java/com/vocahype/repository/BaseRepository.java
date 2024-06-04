package com.vocahype.repository;

import com.vocahype.dto.enumeration.TransformerType;
import com.vocahype.util.AliasToBeanNestedResultTransformer;
import com.vocahype.util.AliasToEntityLinkHashMapResultTransformer;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository {
    @PersistenceContext
    protected EntityManager em;

    protected <T> List<T> getRecords(final String sql, final Map<String, Object> parameters,
                                     final Class<T> clazz, final TransformerType type) {
        Query query = prepareQuery(sql, parameters);
        ResultTransformer resultTransformer = null;
        switch (type) {
            case BEAN:
                resultTransformer = Transformers.aliasToBean(clazz);
                break;
            case NESTED_BEAN:
                resultTransformer = new AliasToBeanNestedResultTransformer(clazz);
                break;
            default:
                resultTransformer = new AliasToEntityLinkHashMapResultTransformer();
        }
        return query
                .unwrap(NativeQuery.class)
                .setResultTransformer(resultTransformer)
                .getResultList();
    }

    protected void manipulateRecord(final String sql, final Map<String, Object> parameters) {
        Query query = prepareQuery(sql, parameters);
        query.executeUpdate();
    }

    private Query prepareQuery(final String sql, final Map<String, Object> parameters) {
        Query query = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        return query;
    }

    protected <T> List<T> getRecords(final String sql, final Map<String, Object> parameters, final Class<T> clazz) {
        return getRecords(sql, parameters, clazz, TransformerType.BEAN);
    }

    protected List<Map> getMapRecords(final String sql, final Map<String, Object> parameters) {
        return getRecords(sql, parameters, Map.class, TransformerType.MAP);
    }

    public static Timestamp getTimestamp(final Long epoch) {
        return new Timestamp(epoch);
    }
}
