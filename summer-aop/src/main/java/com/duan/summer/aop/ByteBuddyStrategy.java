package com.duan.summer.aop;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

/**
 * @author 白日
 * @create 2024/1/1 21:48
 * @description
 */

public class ByteBuddyStrategy implements GeneratorProxyStrategy{
    ByteBuddy byteBuddy = new ByteBuddy();
    @Override
    public Object createProxy(Object bean, InvocationHandler handler){
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
        return proxy;
    }
}
