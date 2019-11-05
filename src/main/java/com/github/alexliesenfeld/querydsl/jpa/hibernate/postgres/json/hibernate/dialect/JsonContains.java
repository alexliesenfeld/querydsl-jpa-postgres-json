package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect;

import java.util.List;
import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 */
class JsonContains extends JsonSQLFunction implements SQLFunction {

  @Override
  protected String doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) throws QueryException {
    StringBuilder sb = new StringBuilder();
    super.buildPath(sb, arguments, -1)
        .append("@>")
        .append(arguments.get(arguments.size() - 1))
        .append("::")
        .append(isJsonb() ? "jsonb" : "json");
    return sb.toString();
  }
}
