package com.duan.summer.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/18 13:41
 * @description
 */

public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
    public T createInterfaceProxy(Map<String, String> sqlSession){
        MapperProxy<T> proxyInvocationHandler = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface}, proxyInvocationHandler);
    }
}
