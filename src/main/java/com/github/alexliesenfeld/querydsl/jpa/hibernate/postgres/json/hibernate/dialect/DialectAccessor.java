package com.github.alexliesenfeld.querydsl.jpa.hibernate.postgres.json.hibernate.dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.joor.Reflect;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/13
 */
public interface DialectAccessor {

  static DialectAccessor accessor(Dialect dialect) {
    DialectAccessor accessor;
    if (dialect instanceof DialectAccessor) {
      accessor = (DialectAccessor) dialect;
    } else {
      accessor = Reflect.on(dialect).as(DialectAccessor.class);
    }
    return accessor;
  }

  void registerColumnType(int code, String name);

  void registerFunction(String name, SQLFunction function);
}
