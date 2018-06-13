package com.github.wenerme.postjava.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
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
public class PostgreSQLJsonDialect extends PostgreSQL95Dialect implements DialectAccessor {

  public PostgreSQLJsonDialect() {
    super();
    register(this);
  }

  public static void register(Dialect dialect) {
    DialectAccessor accessor = DialectAccessor.accessor(dialect);

    accessor.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    accessor.registerColumnType(Types.JAVA_OBJECT, "json");

    // same for json and jsonb
    accessor.registerFunction("hql_json_text", new JsonText());
    accessor.registerFunction("hql_json_int", new JsonText(IntegerType.INSTANCE, "int"));
    accessor.registerFunction("hql_json_float", new JsonText(FloatType.INSTANCE, "float"));

    // jsonb & json functions
    registerJsonFunction(accessor, "array_length", IntegerType.INSTANCE);
    registerJsonFunction(accessor, "typeof", StringType.INSTANCE);

    // Only support jsonb
    accessor.registerFunction("hql_jsonb_contain", new JsonContain().setJsonb(true));
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
