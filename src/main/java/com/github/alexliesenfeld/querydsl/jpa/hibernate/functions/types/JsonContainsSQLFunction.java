package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.types;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.functions.AbstractJsonSQLFunction;

import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.List;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
public class JsonContainsSQLFunction extends AbstractJsonSQLFunction {

  @Override
  protected void doRender(SqlAppender sb, List<? extends SqlAstNode> arguments, SqlAstTranslator<?> walker) {

    super.buildPath(sb, arguments, -1, walker);
    sb.append("@>");
    arguments.get(arguments.size() - 1).accept(walker);
    sb.append("::");
    sb.append(isJsonb() ? "jsonb" : "json");
  }

}
