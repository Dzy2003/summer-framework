package com.duan.summer.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 白日
 * @create 2024/1/4 18:48
 * @description
 */

public class DynamicAopProxy implements InvocationHandler {
    Object target;

    public Map<Class<? extends Annotation>, List<Advice>> proxyRule = new ConcurrentHashMap<>();
    List<Advice> proxyChains = new ArrayList<>(8);
    int chainsIndex = 0;
    public DynamicAopProxy(Map<Class<? extends Annotation>, List<Advice>> proxyRule, Object target){
        this.proxyRule = proxyRule;
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(chainsIndex == 0){
            List<Advice> curProxyChains = new ArrayList<>(8);
            proxyRule.forEach((key, value) -> {
                if (target.getClass().isAnnotationPresent(key) ||
                        method.isAnnotationPresent(key)) {
                    curProxyChains.addAll(value);
                }
            });
            this.proxyChains = curProxyChains;
        }
        if(proxyChains.size() == chainsIndex){
            chainsIndex = 0;
            return method.invoke(target, args);
        }
        //构建参数
        ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint();
        proceedingJoinPoint.setArgs(args);
        proceedingJoinPoint.setMethod(method);
        proceedingJoinPoint.setProxyChains(proxyChains);
        proceedingJoinPoint.setTarget(proxy);//递归，proxy再次调用该方法会再次转发到invoke方法
        Advice advice = proxyChains.get(chainsIndex++);
        proceedingJoinPoint.setChainsIndex(chainsIndex);
        advice.setArgs(new Object[]{proceedingJoinPoint});
        return advice.invoker();
    }
}
