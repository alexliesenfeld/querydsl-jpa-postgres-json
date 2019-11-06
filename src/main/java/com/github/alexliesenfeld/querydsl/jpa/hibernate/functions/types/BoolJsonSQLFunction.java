package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.types;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.AbstractTypedJsonFunction;
import org.hibernate.type.BooleanType;

/**
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class BoolJsonSQLFunction extends AbstractTypedJsonFunction {
    public BoolJsonSQLFunction() {
        super(BooleanType.INSTANCE, "boolean");
    }
}
