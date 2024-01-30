package com.duan.summer.session.defaults;

import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.Environment;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/20 22:20
 * @description
 */

public class DefaultsSqlSession implements SqlSession {
    Configuration configuration;
    private Executor executor;
    public DefaultsSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的操作被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object[] parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        List<Object> query = executor.query(mappedStatement, parameter, null, mappedStatement.getBoundSql());
        return(T) query.get(0);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
