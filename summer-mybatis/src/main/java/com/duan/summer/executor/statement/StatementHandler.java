package com.duan.summer.executor.statement;
import com.duan.summer.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    /**
     * 准备语句
     */
    Statement prepare(Connection connection) throws SQLException;

    /**
     * 参数化
     */
    void parameterize(Statement statement) throws SQLException, IllegalAccessException;

    /**
     * 执行查询
     */
    <E> List<E> query(Statement statement) throws SQLException;

    /**
     *执行更新操作
     */
    int update(Statement statement) throws SQLException;

}
