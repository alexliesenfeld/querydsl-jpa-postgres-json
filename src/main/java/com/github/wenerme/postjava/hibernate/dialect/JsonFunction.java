package com.github.wenerme.postjava.hibernate.dialect;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 */
@Setter
@Getter
class JsonFunction extends JsonSQLFunction implements Cloneable {

  protected String functionName;
  protected String jsonbFunctionName;
  protected String jsonFunctionName;
  protected Type type;

  JsonFunction() {
    setMinimalArgumentCount(1);
  }

  public JsonFunction(Type type, String functionName) {
    this();
    setFunctionName(functionName);
  }

  @Override
  public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
    return type;
  }

  protected String doRender(
      Type firstArgumentType, List arguments, SessionFactoryImplementor factory)
      throws QueryException {

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

  @Override
  public JsonFunction clone() {
    try {
      return (JsonFunction) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
