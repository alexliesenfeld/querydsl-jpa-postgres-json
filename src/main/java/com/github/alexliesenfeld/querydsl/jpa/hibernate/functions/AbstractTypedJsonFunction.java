package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import java.util.List;

import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public abstract class AbstractTypedJsonFunction extends AbstractJsonSQLFunction {
  private final String conversion;
  private final Type type;

  public AbstractTypedJsonFunction(Type type, String conversion) {
    this.conversion = conversion;
    this.type = type;
  }

  @Override
  public Type getReturnType(Type firstArgumentType, Mapping mapping) {
    return type;
  }

  protected CharSequence doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) {
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
