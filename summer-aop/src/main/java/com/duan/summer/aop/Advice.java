package com.duan.summer.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 白日
 * @create 2024/1/4 18:24
 * @description 通知方法
 */

public class Advice {
    /**
     * 切点的方法
     */
    private Method method;
    /**
     * 切点的对象
     */
    private Object target;
    /**
     * 切点的参数
     */
    private Object[] args;

    public Advice(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public Object invoker() throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}