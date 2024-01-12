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

    public Map<String, List<Advice>> proxyRule = new ConcurrentHashMap<>();
    int chainsIndex = 0;
    public DynamicAopProxy(Map<String, List<Advice>> proxyRule, Object target){
        this.proxyRule = proxyRule;
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = target.getClass()
                .getMethod(method.getName(), method.getParameterTypes());
        Annotation[] targetAnnotations = targetMethod.getDeclaredAnnotations();
        //没注解  不代理
        if (targetAnnotations.length == 0) {
            return method.invoke(target, args);
        }
        List<Advice> proxyChains = new ArrayList<>(8);
        Arrays.stream(targetAnnotations).forEach(annotation -> {
            if (proxyRule.containsKey(annotation.annotationType().getSimpleName())) {
                proxyChains.addAll(proxyRule.get(annotation.annotationType().getSimpleName()));
            }
        });
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
