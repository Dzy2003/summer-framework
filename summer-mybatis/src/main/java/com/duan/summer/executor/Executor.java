package com.duan.summer.executor;


import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.ResultHandler;
import com.duan.summer.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 小傅哥，微信：fustack
 * @description 执行器
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object[] parameters);
    int update(MappedStatement ms, Object[] parameters);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}