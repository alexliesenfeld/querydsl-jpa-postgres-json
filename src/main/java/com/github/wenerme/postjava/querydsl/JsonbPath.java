package com.github.wenerme.postjava.querydsl;

import com.github.wenerme.wava.util.JSON;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import java.util.List;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/13
 */
public class JsonbPath extends JsonPath {

  protected JsonbPath(Path<?> parent, String property) {
    super(parent, property);
  }

  public static JsonbPath of(Path<?> path, String... properties) {
    JsonbPath p = new JsonbPath(path, ".");
    for (String property : properties) {
      p = of(p, property);
    }
    return p;
  }

  public static JsonbPath of(Path<?> path, String property) {
    return new JsonbPath(path, property);
  }

  @Override
  protected boolean isJsonb() {
    return true;
  }

  /** JSONB {@code @>} syntax */
  public BooleanExpression contain(Object value) {
    List<Object> args = properties();
    args.add(JSON.stringify(value));
    StringBuilder sb = new StringBuilder();
    functionNameOf(sb, "contain").append('(');
    generateArgs(sb, args.size());
    sb.append(')');
    return Expressions.booleanTemplate(sb.toString(), args);
  }
}
