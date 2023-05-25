package com.github.alexliesenfeld.querydsl.jpa.hibernate;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.types.*;
import io.hypersistence.utils.hibernate.type.json.internal.JsonBinaryJdbcTypeDescriptor;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.service.ServiceRegistry;

import java.sql.Types;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class PostgreSQLJsonDialect extends PostgresPlusDialect {

  /**
   * You can either directly use it or derive your custom dialect by using this constructor.
   */
  public PostgreSQLJsonDialect() {
    super();
  }


  @Override
  public void initializeFunctionRegistry(QueryEngine queryEngine) {
    super.initializeFunctionRegistry(queryEngine);

    queryEngine.getSqmFunctionRegistry().register("hql_json_text", new TextJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_int", new IntJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_float", new FloatJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_double", new DoubleJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_long", new LongJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_short", new ShortJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_json_bool", new BoolJsonSQLFunction());

    queryEngine.getSqmFunctionRegistry().register("hql_json_" + "array_length", new LongJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_jsonb_" + "array_length", new LongJsonSQLFunction().setJsonb(true));

    queryEngine.getSqmFunctionRegistry().register("hql_json_" + "typeof", new LongJsonSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_jsonb_" + "typeof", new LongJsonSQLFunction().setJsonb(true));


    queryEngine.getSqmFunctionRegistry().register("hql_json_contains", new JsonContainsSQLFunction());
    queryEngine.getSqmFunctionRegistry().register("hql_jsonb_contains", new JsonContainsSQLFunction().setJsonb(true));

  }

  @Override
  public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    super.contributeTypes(typeContributions, serviceRegistry);
    typeContributions.getTypeConfiguration().getJdbcTypeRegistry().addDescriptor(Types.JAVA_OBJECT, new JsonBinaryJdbcTypeDescriptor());
    typeContributions.getTypeConfiguration().getJdbcTypeRegistry().addDescriptor(Types.JAVA_OBJECT, new JsonBinaryJdbcTypeDescriptor());
  }
}
