package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect;

import java.util.List;
import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 */
class JsonText extends JsonSQLFunction {
  private String conversion;
  private Type type;

  JsonText() {
    this(StringType.INSTANCE, null);
  }

  JsonText(Type type, String conversion) {
    this.conversion = conversion;
  }

  @Override
  public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
    return type;
  }

  protected CharSequence doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) throws QueryException {
    StringBuilder sb = new StringBuilder();

    if (conversion != null) {
      sb.append('(');
    }

    buildPath(sb, arguments, -1).append("->>").append(arguments.get(arguments.size() - 1));

    if (conversion != null) {
      sb.append(")::").append(conversion);
    }

    return sb;
  }
}
