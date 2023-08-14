package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.AssertionFailure;
import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.ColumnReference;
import org.hibernate.sql.ast.tree.predicate.Predicate;
import org.hibernate.sql.ast.tree.update.Assignable;
import org.hibernate.type.JavaObjectType;

import java.util.List;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
 * @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
 */
@Setter
@Getter
public abstract class AbstractJsonSQLFunction
        extends AbstractSqmSelfRenderingFunctionDescriptor {

  static int minimalArgumentCount = 2;
  static int maximalArgumentCount = 64;
  protected boolean jsonb = true;


  public void buildPath(SqlAppender sb,  List<? extends SqlAstNode>  arguments, SqlAstTranslator<?> walker) {
    buildPath(sb, arguments, 0, arguments.size(), walker);
  }

  public void buildPath(SqlAppender sb, List<? extends SqlAstNode>  arguments, int n, SqlAstTranslator<?> walker) {
    buildPath(sb, arguments, 0, n < 0 ? arguments.size() + n : n, walker);
  }

  public void buildPath(SqlAppender sb, List<? extends SqlAstNode>  arguments, int from, int to, SqlAstTranslator<?> walker) {
    Object arg = arguments.get(from);

    if (!(arg instanceof Assignable))
      throw new AssertionFailure("Not assignable");

    ColumnReference columnReference = ((Assignable) arg).getColumnReferences().get(0);
    sb.append(columnReference.getExpressionText());

    for (int i = to - 1; i >= from + 1; i--) {
      sb.append("->");
      arguments.get(i).accept(walker);
    }
  }

  protected AbstractJsonSQLFunction() {
    super(
            "sql",
            StandardArgumentsValidators.between(minimalArgumentCount, maximalArgumentCount),
            StandardFunctionReturnTypeResolvers.invariant(JavaObjectType.INSTANCE),
            null
    );
  }

  @Override

  public void render(
          SqlAppender sqlAppender,
          List<? extends SqlAstNode> sqlAstArguments,
          Predicate filter,
          Boolean respectNulls,
          Boolean fromFirst,
          SqlAstTranslator<?> walker) {
    doRender(sqlAppender, sqlAstArguments, walker);
  }

  public void render(
          SqlAppender sqlAppender,
          List<? extends SqlAstNode> arguments,
          SqlAstTranslator<?> walker) {
    doRender(sqlAppender, arguments, walker);
  }

  protected abstract void doRender(SqlAppender sb, List<? extends SqlAstNode> arguments, SqlAstTranslator<?> walker);

  @Override
  public String getArgumentListSignature() {
    return "";
  }
}