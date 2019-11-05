package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect;

import java.sql.Types;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.*;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types.JsonContainsSQLFunction;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types.FloatJsonSQLFunction;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types.IntJsonSQLFunction;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect.functions.types.TextJsonSQLFunction;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class PostgreSQLJsonDialect extends PostgreSQL95Dialect {

  public PostgreSQLJsonDialect() {
    super();
    register();
  }

  public void register() {
    registerColumnType(Types.JAVA_OBJECT, "jsonb");
    registerColumnType(Types.JAVA_OBJECT, "json");

    registerFunction("hql_json_text", new TextJsonSQLFunction());
    registerFunction("hql_json_int", new IntJsonSQLFunction());
    registerFunction("hql_json_float", new FloatJsonSQLFunction());

    registerJsonFunction("array_length", IntegerType.INSTANCE);
    registerJsonFunction("typeof", StringType.INSTANCE);

    registerFunction("hql_jsonb_contains", new JsonContainsSQLFunction().setJsonb(true));
  }

  void registerJsonFunction(String name, Type type) {
    registerFunction("hql_json_" + name, new JsonFunction(type, name));
    registerFunction("hql_jsonb_" + name, new JsonFunction(type, name).setJsonb(true));
  }

}
