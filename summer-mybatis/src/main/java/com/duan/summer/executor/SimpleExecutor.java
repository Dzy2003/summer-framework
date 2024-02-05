package com.duan.summer.executor;

import com.duan.summer.executor.statement.StatementHandler;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.ResultHandler;
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

public class SimpleExecutor extends BaseExecutor{
    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object[] parameters) {
        try {
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameters);
            Connection connection = transaction.getConnection();
            Statement stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.query(stmt);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
