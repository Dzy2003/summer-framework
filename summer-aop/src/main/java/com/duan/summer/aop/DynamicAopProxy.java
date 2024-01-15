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
    Object target;//原始对象

    public Map<Class<? extends Annotation>, List<Advice>> proxyRule = new ConcurrentHashMap<>();//拦截规则
    List<Advice> proxyChains = new ArrayList<>(8); //当前Advice调用链
    int chainsIndex = 0; //调用链指针
    public DynamicAopProxy(Map<Class<? extends Annotation>, List<Advice>> proxyRule, Object target){
        this.proxyRule = proxyRule;
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(chainsIndex == 0){
            getCurProxyChains(method);
        }
        //递归中止条件
        if(proxyChains.size() == chainsIndex){
            chainsIndex = 0; //清空调用链
            proxyChains = new ArrayList<>(8); //清空调用链
            return method.invoke(target, args);
        }
        //构造当前的Advice并调用
        Advice advice = getAdvice(proxy, method, args);
        return advice.invoker();//递归入口，在advice中调用method.invoke(proxy,args)仍然会转发到到invoke方法InvocationHandler的invoke方法
    }

    private Advice getAdvice(Object proxy, Method method, Object[] args) {
        //构建参数
        ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint();
        proceedingJoinPoint.setArgs(args);
        proceedingJoinPoint.setMethod(method);
        proceedingJoinPoint.setProxyChains(proxyChains);
        proceedingJoinPoint.setTarget(proxy);//我们传入proxy代理对象
        Advice advice = proxyChains.get(chainsIndex++);//指针指向下一个Advice
        proceedingJoinPoint.setChainsIndex(chainsIndex);
        advice.setArgs(new Object[]{proceedingJoinPoint});
        return advice;
    }

    private void getCurProxyChains(Method method) {
        List<Advice> curProxyChains = new ArrayList<>(8);
        proxyRule.forEach((key, value) -> {
            if (target.getClass().isAnnotationPresent(key) ||
                    method.isAnnotationPresent(key)) {
                curProxyChains.addAll(value);
            }
        });
        this.proxyChains = curProxyChains;
    }
}
