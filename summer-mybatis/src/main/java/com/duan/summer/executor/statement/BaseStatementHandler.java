package com.duan.summer.executor.statement;

import com.duan.summer.executor.Executor;
import com.duan.summer.executor.resultset.ResultSetHandler;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 13:08
 * @description
 */

public abstract class BaseStatementHandler implements StatementHandler{
    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }
    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        }catch (Exception e){
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }
    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
