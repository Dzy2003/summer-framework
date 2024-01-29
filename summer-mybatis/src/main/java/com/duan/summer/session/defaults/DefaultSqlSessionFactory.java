package com.duan.summer.session.defaults;

import com.duan.summer.binding.MapperRegistry;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;

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
        return new DefaultsSqlSession(configuration);
    }
}
