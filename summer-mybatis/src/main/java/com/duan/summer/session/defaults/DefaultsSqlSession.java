package com.duan.summer.session.defaults;

import com.duan.summer.binding.MapperRegistry;
import com.duan.summer.session.SqlSession;

/**
 * @author 白日
 * @create 2024/1/20 22:20
 * @description
 */

public class DefaultsSqlSession implements SqlSession {
    MapperRegistry mapperRegistry;
    public DefaultsSqlSession(MapperRegistry mapperRegistry){
        this.mapperRegistry = mapperRegistry;
    }
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的操作被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你的操作被代理了！" + "方法：" + statement + " 入参：" + parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
