package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types;

import java.util.List;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.AbstractJsonSQLFunction;
import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class JsonContainsSQLFunction extends AbstractJsonSQLFunction implements SQLFunction {

  @Override
  protected String doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) {
    StringBuilder sb = new StringBuilder();
    super.buildPath(sb, arguments, -1)
        .append("@>")
        .append(arguments.get(arguments.size() - 1))
        .append("::")
        .append(isJsonb() ? "jsonb" : "json");
    return sb.toString();
  }

}
