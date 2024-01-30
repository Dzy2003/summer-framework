package com.duan.summer.session.defaults;

import com.duan.summer.binding.MapperRegistry;
import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.Environment;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.session.TransactionIsolationLevel;
import com.duan.summer.transaction.Transaction;
import com.duan.summer.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @author 白日
 * @create 2024/1/20 22:21
 * @description
 */

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DefaultsSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
