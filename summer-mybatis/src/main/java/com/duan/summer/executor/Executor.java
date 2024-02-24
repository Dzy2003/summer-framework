package com.duan.summer.executor;


import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.ResultHandler;
import com.duan.summer.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(MappedStatement ms, Object[] parameters);
    int update(MappedStatement ms, Object[] parameters);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}