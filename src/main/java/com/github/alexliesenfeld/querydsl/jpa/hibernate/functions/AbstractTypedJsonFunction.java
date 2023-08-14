package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;

import java.util.List;

/**
* @author <a href=http://github.com/wenerme>wener</a>
* @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
* @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
*/
public abstract class AbstractTypedJsonFunction extends AbstractJsonSQLFunction {
    private final String conversion;

    protected AbstractTypedJsonFunction(String conversion) {
        this.conversion = conversion;
    }

    protected void doRender(SqlAppender sb, List<? extends SqlAstNode> arguments, SqlAstTranslator<?> walker) {
        if (conversion != null) {
            sb.append('(');
        }

        buildPath(sb, arguments, -1, walker);
        sb.append("->>");

        arguments.get(arguments.size() - 1).accept(walker);

        if (conversion != null) {
            sb.append(")::");
            sb.append(conversion);
        }
    }
}
