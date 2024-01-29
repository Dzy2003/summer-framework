package com.duan.summer.session.defaults;

import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;

/**
 * @author 白日
 * @create 2024/1/20 22:20
 * @description
 */

public class DefaultsSqlSession implements SqlSession {
    Configuration configuration;
    public DefaultsSqlSession(Configuration configuration){
        this.configuration = configuration;
    }
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的操作被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你的操作被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
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
