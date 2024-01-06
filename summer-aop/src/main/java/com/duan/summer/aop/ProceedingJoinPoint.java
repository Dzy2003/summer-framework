package com.duan.summer.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/4 18:45
 * @description
 */

public class ProceedingJoinPoint {
    /**
     * 目标方法的参数
     */
    private Object[] args;
    /**
     * 目标对象
     */
    private Object target;
    /**
     * 目标方法
     */
    private Method method;
    /**
     * 存在的调用链
     */
    private List<Advice> proxyChains = new ArrayList<>(8);
    /**
     * 当前调用链的指针位置
     */
    private int chainsIndex = 0;


    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target,args);
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setProxyChains(List<Advice> proxyChains) {
        this.proxyChains = proxyChains;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Advice> getProxyChains() {
        return proxyChains;
    }

    public int getChainsIndex() {
        return chainsIndex;
    }

    public void setChainsIndex(int chainsIndex) {
        this.chainsIndex = chainsIndex;
    }
}
