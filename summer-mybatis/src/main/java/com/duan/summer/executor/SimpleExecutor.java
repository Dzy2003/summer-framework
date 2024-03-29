package com.duan.summer.executor;

import com.duan.summer.executor.statement.StatementHandler;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 13:33
 * @description
 */

public class SimpleExecutor extends AbstractExecutor {
    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object[] parameters) {
        Statement stmt = null;
        try {
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameters);
            Connection connection = transaction.getConnection();
            stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.query(stmt);
        } catch (SQLException | IllegalAccessException e) {
            return null;
        }finally {
            closeStatement(stmt);
        }
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object[] parameters) {
        Statement stmt = null;
        try {
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameters);
            Connection connection = transaction.getConnection();
            stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.update(stmt);
        } catch (SQLException | IllegalAccessException e) {
            return 0;
        }finally {
            closeStatement(stmt);
        }
    }
}
