package com.duan.summer.aop;

import java.lang.reflect.InvocationHandler;

/**
 * @author 白日
 * @create 2024/1/1 21:43
 * @description
 */

public interface GeneratorProxyStrategy {
    Object createProxy(Object bean, InvocationHandler handler);
}
