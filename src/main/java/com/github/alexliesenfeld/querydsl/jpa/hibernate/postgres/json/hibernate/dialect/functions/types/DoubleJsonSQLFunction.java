package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.AbstractTypedJsonFunction;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class DoubleJsonSQLFunction extends AbstractTypedJsonFunction {
    public DoubleJsonSQLFunction() {
        super(DoubleType.INSTANCE, "float8");
    }
}
