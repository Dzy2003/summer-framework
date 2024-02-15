package com.duan.summer.transaction.jdbc;


import com.duan.summer.session.TransactionIsolationLevel;
import com.duan.summer.transaction.Transaction;
import com.duan.summer.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;


public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }

}
