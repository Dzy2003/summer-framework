package com.duan.summer.aop;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

/**
 * @author 白日
 * @create 2024/1/1 16:06
 * @description
 */

public class ProxyResolver {
    ByteBuddy byteBuddy = new ByteBuddy();
    public <T> T createProxy(T bean, InvocationHandler handler){
        Class<?> targetClass = bean.getClass();
        Class<?> proxyClass = byteBuddy
                .subclass(targetClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                .method(ElementMatchers.isPublic())
                .intercept(InvocationHandlerAdapter.of(
                        (o, method, args) -> handler.invoke(bean, method, args)))
                .make()
                .load(targetClass.getClassLoader())
                .getLoaded();
        Object proxy = null;
        try {
            proxy = proxyClass.getConstructor().newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return (T) proxy;
    }
}
