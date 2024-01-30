package com.duan.summer.executor.statement;

import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.ResultHandler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 13:10
 * @description
 */

public class PreparedStatementHandler extends BaseStatementHandler{
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());

    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.setLong(1, Long.parseLong(((Object[]) parameterObject)[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }
}
