package com.github.alexliesenfeld.querydsl.jpa.hibernate.functions;

import org.hibernate.sql.ast.spi.SqlAppender;

import java.util.List;

/**
* @author <a href=http://github.com/wenerme>wener</a>
* @author <a href=http://github.com/alexliesenfeld>Alexander Liesenfeld</a>
* @see <a href=https://www.postgresql.org/docs/current/static/functions-json.html>functions-json</a>
*/
public abstract class AbstractTypedJsonFunction extends AbstractJsonSQLFunction {
    private final String conversion;

    public AbstractTypedJsonFunction(String conversion) {
        this.conversion = conversion;
    }

    protected void doRender(SqlAppender sb, List arguments) {
        if (conversion != null) {
            sb.append('(');
        }

        Object arg = arguments.get(arguments.size() - 1);
        buildPath(sb, arguments, -1);
        sb.append("->>");

        sb.append("'");
        sb.append(arg == null ? "null" : getSqmParameterInterpretation(arg).toString());
        sb.append("'");

        if (conversion != null) {
            sb.append(")::");
            sb.append(conversion);
        }
    }
}
