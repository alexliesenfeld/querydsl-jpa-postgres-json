package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import com.google.common.base.Preconditions;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
@Setter
@Getter
public abstract class AbstractJsonSQLFunction implements SQLFunction {
  protected int minimalArgumentCount = 2;
  protected int maximalArgumentCount = 64;
  protected boolean jsonb;

  @Override
  public boolean hasArguments() {
    return true;
  }

  @Override
  public boolean hasParenthesesIfNoArguments() {
    return false;
  }

  @Override
  public Type getReturnType(Type firstArgumentType, Mapping mapping) {
    return BooleanType.INSTANCE;
  }

  public StringBuilder buildPath(StringBuilder sb, List arguments) {
    return buildPath(sb, arguments, 0, arguments.size());
  }

  public StringBuilder buildPath(StringBuilder sb, List arguments, int n) {
    return buildPath(sb, arguments, 0, n < 0 ? arguments.size() + n : n);
  }

  public StringBuilder buildPath(StringBuilder sb, List arguments, int from, int to) {
    sb.append(arguments.get(from));
    for (int i = from + 1; i < to; i++) {
      sb.append("->").append(arguments.get(i));
    }
    return sb;
  }

  @Override
  public final String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory){
    int argc = arguments.size();
    Preconditions.checkArgument(argc >= minimalArgumentCount, "At least %s arguments got %s", minimalArgumentCount, argc);
    Preconditions.checkArgument(argc <= maximalArgumentCount, "At most %s arguments got %s", maximalArgumentCount, argc);
    return doRender(firstArgumentType, arguments, factory).toString();
  }

  protected abstract CharSequence doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory);
}
