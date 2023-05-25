package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.AssertionFailure;
import org.hibernate.query.spi.QueryParameterBinding;
import org.hibernate.query.spi.QueryParameterImplementor;
import org.hibernate.query.sqm.function.AbstractSqmSelfRenderingFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.sql.internal.SqmParameterInterpretation;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.ColumnReference;
import org.hibernate.sql.ast.tree.predicate.Predicate;
import org.hibernate.sql.ast.tree.update.Assignable;
import org.hibernate.type.JavaObjectType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

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

  static Field queryParamField;
  static Field queryParamBindingResolverField;

  static {
    try {
      queryParamField = SqmParameterInterpretation.class.getDeclaredField("queryParameter");
      queryParamField.setAccessible(true);
      queryParamBindingResolverField = SqmParameterInterpretation.class.getDeclaredField("queryParameterBindingResolver");
      queryParamBindingResolverField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }


  protected Object getSqmParameterInterpretation(Object arg) {
    if (!(arg instanceof SqmParameterInterpretation))
      throw new AssertionFailure("Not an implemented parameter type");
    try {
      Object queryParam = queryParamField.get(arg);
      Object queryParamBindingResolver = queryParamBindingResolverField.get(arg);

      QueryParameterBinding<?> binding = ((Function<QueryParameterImplementor<?>, QueryParameterBinding<?>>) queryParamBindingResolver).apply((QueryParameterImplementor<?>) queryParam);
      return binding.getBindValue();
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void buildPath(SqlAppender sb, List arguments) {
    buildPath(sb, arguments, 0, arguments.size());
  }

  public void buildPath(SqlAppender sb, List arguments, int n) {
    buildPath(sb, arguments, 0, n < 0 ? arguments.size() + n : n);
  }

  public void buildPath(SqlAppender sb, List arguments, int from, int to) {
    Object arg = arguments.get(from);

    if (!(arg instanceof Assignable))
      throw new AssertionFailure("Not assignable");

    ColumnReference columnReference = ((Assignable) arg).getColumnReferences().get(0);
    sb.append(columnReference.getExpressionText());

    for (int i = to - 1; i >= from + 1; i--) {
      sb.append("->");

      sb.append("'");
      sb.append(arg == null ? "null" : getSqmParameterInterpretation(arguments.get(i)).toString());
      sb.append("'");
    }
  }

  public AbstractJsonSQLFunction() {
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
    doRender(sqlAppender, sqlAstArguments);
  }

  public void render(
          SqlAppender sqlAppender,
          List<? extends SqlAstNode> arguments,
          SqlAstTranslator<?> walker) {
    doRender(sqlAppender, arguments);
  }

  protected abstract void doRender(SqlAppender sb, List arguments);

  @Override
  public String getArgumentListSignature() {
    return "";
  }
}