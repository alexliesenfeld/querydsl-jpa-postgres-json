package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
@Setter
@Getter
public class JsonFunction extends AbstractJsonSQLFunction {
  protected String functionName;
  protected String jsonbFunctionName;
  protected String jsonFunctionName;
  protected Type type;

  JsonFunction() {
    super();
    setMinimalArgumentCount(1);
  }

  public JsonFunction(Type type, String functionName) {
    this();
    this.type = type;
    setFunctionName(functionName);
  }

  @Override
  public Type getReturnType(Type firstArgumentType, Mapping mapping) {
    return type;
  }

  protected String doRender(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) {
    StringBuilder sb = new StringBuilder();
    sb.append(isJsonb() ? jsonbFunctionName : jsonFunctionName).append('(');
    buildPath(sb, arguments);
    sb.append(')');
    return sb.toString();
  }

  public JsonFunction setFunctionName(String functionName) {
    this.functionName = functionName;
    jsonFunctionName = "json_" + functionName;
    jsonbFunctionName = "jsonb_" + functionName;
    return this;
  }

}
