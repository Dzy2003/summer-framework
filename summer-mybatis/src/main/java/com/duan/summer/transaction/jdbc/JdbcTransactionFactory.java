package com.duan.summer.transaction.jdbc;


import com.duan.summer.session.TransactionIsolationLevel;
import com.duan.summer.transaction.Transaction;
import com.duan.summer.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author 小傅哥，微信：fustack
 * @description JdbcTransaction 工厂
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
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
