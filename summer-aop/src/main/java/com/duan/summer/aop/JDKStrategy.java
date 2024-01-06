package com.duan.summer.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author 白日
 * @create 2024/1/1 21:58
 * @description
 */

public class JDKStrategy implements GeneratorProxyStrategy{
    @Override
    public Object createProxy(Object bean, InvocationHandler handler) {
        return  Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                handler);
    }
}
