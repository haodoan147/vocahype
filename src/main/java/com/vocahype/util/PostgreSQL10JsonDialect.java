package com.vocahype.util;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class PostgreSQL10JsonDialect extends PostgreSQL10Dialect {

    public PostgreSQL10JsonDialect() {
        super();
        this.registerHibernateType(Types.OTHER, JsonNodeBinaryType.class.getName());
        this.registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
    }
}
