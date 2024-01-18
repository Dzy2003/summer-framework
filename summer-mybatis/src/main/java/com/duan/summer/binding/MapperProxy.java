package com.duan.summer.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/18 13:41
 * @description
 */

public class MapperProxy<T> implements InvocationHandler, Serializable {
    private final Map<String, String> sqlSession;
    private final Class<?> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<?> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this, args);
        }else{
            return "代理成功!" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
        }
    }
}
