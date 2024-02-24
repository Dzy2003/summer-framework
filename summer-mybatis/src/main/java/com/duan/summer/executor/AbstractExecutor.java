package com.duan.summer.executor;

import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 12:33
 * @description
 */

public abstract class AbstractExecutor implements Executor{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractExecutor.class);
    Configuration configuration;
    Transaction transaction;
    private boolean closed;

    public AbstractExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object[] parameters) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameters);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object[] parameters);

    @Override
    public int update(MappedStatement ms, Object[] parameters) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doUpdate(ms, parameters);
    }

    protected abstract int doUpdate(MappedStatement ms, Object[] parameters);

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if(closed){
            throw new RuntimeException("Executor was closed.");
        }
        if(required){
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if(closed){
            throw new RuntimeException("Executor was closed.");
        }
        if(required){
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }
    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }
}
