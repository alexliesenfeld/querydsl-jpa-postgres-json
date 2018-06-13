package com.github.wenerme.postjava.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/12
 * @see <a
 *     href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class PostgreSQLJsonDialect extends PostgreSQLDialect implements DialectAccessor {

  public PostgreSQLJsonDialect() {
    super();
    register(this);
  }

  public static void register(DialectAccessor dialect) {

    dialect.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    dialect.registerColumnType(Types.JAVA_OBJECT, "json");

    // same for json and jsonb
    dialect.registerFunction("hql_json_text", new JsonText());
    dialect.registerFunction("hql_json_int", new JsonText(IntegerType.INSTANCE, "int"));
    dialect.registerFunction("hql_json_float", new JsonText(FloatType.INSTANCE, "float"));

    // jsonb & json functions
    registerJsonFunction(dialect, "array_length", IntegerType.INSTANCE);
    registerJsonFunction(dialect, "typeof", StringType.INSTANCE);

    // Only support jsonb
    dialect.registerFunction("hql_jsonb_contain", new JsonContain().setJsonb(true));
  }

  static void registerJsonFunction(DialectAccessor dialect, String name, JsonFunction function) {
    dialect.registerFunction("hql_json_" + name, function);
    dialect.registerFunction("hql_jsonb_" + name, function.clone().setJsonb(true));
  }

  static void registerJsonFunction(DialectAccessor dialect, String name, Type type) {
    registerJsonFunction(dialect, name, new JsonFunction(type, name));
  }

  @Override
  public void registerColumnType(int code, String name) {
    super.registerColumnType(code, name);
  }

  @Override
  public void registerFunction(String name, SQLFunction function) {
    super.registerFunction(name, function);
  }
}
