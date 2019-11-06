package com.github.alexliesenfeld.querydsl.jpa.hibernate;

import java.sql.Types;
import java.util.function.BiConsumer;


import com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.JsonFunction;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.types.*;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class PostgreSQLJsonDialect extends PostgreSQL95Dialect {
  private final BiConsumer<Integer, String> registerColumnTypeConsumer;
  private final BiConsumer<String, SQLFunction> registerFunctionConsumer;

  /**
   * You can either directly use it or derive your custom dialect by using this constructor.
   */
  public PostgreSQLJsonDialect() {
    super();

    this.registerColumnTypeConsumer = this::registerColumnType;
    this.registerFunctionConsumer = this::registerFunction;

    register();
  }

  /**
   * Use this constructor if you cannot use or derive from this dialect class. You can create a new instance by
   * using this constructor and register the provided functionality by calling the
   * {@link PostgreSQLJsonDialect#register()} function. This will still allow you to use the functionality provided
   * by this dialect but you do not need to use or derive from this dialect class.
   *
   * @param registerColumnTypeConsumer A reference to the {@link PostgreSQL95Dialect#registerColumnType(int, String)}
   *                                   function this class should be using. If you do not have any custom functions
   *                                   for this, just pass <code>this::registerColumnType</code>, where
   *                                   <code>this</code> refers to your dialect class calling this constructor.
   *
   * @param registerFunctionConsumer A reference to the
   *                                 {@link PostgreSQL95Dialect#registerFunction(String, SQLFunction)} (int, String)}
   *                                 function this class should be using. If you do not have any custom functions for
   *                                 this, just pass <code>this::registerFunction</code>, where <code>this</code>
   *                                 refers to your dialect class calling this constructor.
   */
  public PostgreSQLJsonDialect(BiConsumer<Integer, String> registerColumnTypeConsumer, BiConsumer<String, SQLFunction> registerFunctionConsumer) {
    super();

    this.registerColumnTypeConsumer = registerColumnTypeConsumer;
    this.registerFunctionConsumer = registerFunctionConsumer;

    register();
  }

  public void register() {
    this.registerColumnTypeConsumer.accept(Types.JAVA_OBJECT, "jsonb");
    this.registerColumnTypeConsumer.accept(Types.JAVA_OBJECT, "json");

    this.registerFunctionConsumer.accept("hql_json_text", new TextJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_int", new IntJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_float", new FloatJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_double", new DoubleJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_long", new LongJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_short", new ShortJsonSQLFunction());
    this.registerFunctionConsumer.accept("hql_json_bool", new BoolJsonSQLFunction());

    this.registerJsonFunction("array_length", IntegerType.INSTANCE);
    this.registerJsonFunction("typeof", StringType.INSTANCE);

    this.registerFunctionConsumer.accept("hql_jsonb_contains", new JsonContainsSQLFunction().setJsonb(true));
  }

  private void registerJsonFunction(String name, Type type) {
    this.registerFunctionConsumer.accept("hql_json_" + name, new JsonFunction(type, name));
    this.registerFunctionConsumer.accept("hql_jsonb_" + name, new JsonFunction(type, name).setJsonb(true));
  }

}
