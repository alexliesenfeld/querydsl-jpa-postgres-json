package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.type.Type;

import java.util.List;

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
  }

  protected void doRender(SqlAppender sb, List arguments) {
    sb.append(isJsonb() ? jsonbFunctionName : jsonFunctionName);
    sb.append('(');
    buildPath(sb, arguments);
    sb.append(')');
  }

}
