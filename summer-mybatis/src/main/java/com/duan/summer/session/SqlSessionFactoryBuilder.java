package com.duan.summer.session;

import com.duan.summer.builder.XMLConfigBuilder;
import com.duan.summer.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @author 白日
 * @create 2024/1/21 15:25
 * @description
 */

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
}
