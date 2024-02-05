package com.duan.summer.executor;

import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.ResultHandler;
import com.duan.summer.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 12:33
 * @description
 */

public abstract class BaseExecutor implements Executor{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);
    Configuration configuration;
    Transaction transaction;
    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object[] parameters) {
        logger.debug("sql:{}", ms.getBoundSql().getSql());
        for (int i = 0; i < parameters.length; i++){
            logger.debug("parameter"+ i + ":{},type:{}", parameters[i],parameters[i].getClass() );
        }
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameters);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object[] parameters);

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
            transaction.rollback();
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
}