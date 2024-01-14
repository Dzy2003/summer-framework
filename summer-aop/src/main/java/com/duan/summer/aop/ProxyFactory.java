package com.duan.summer.aop;
import java.lang.reflect.InvocationHandler;

/**
 * @author 白日
 * @create 2024/1/1 16:06
 * @description 创建代理对象的工厂
 */

public class ProxyFactory {
    GeneratorProxyStrategy proxyStrategy;
    public Object createProxy(Object bean, InvocationHandler handler){
        chooseStrategy(bean);//选择代理策略
        return proxyStrategy.createProxy(bean, handler);//创建实例
    }
    /**
     * 根据对象的接口数量，选择代理策略
     * @param bean 代理对象
     */
    private void chooseStrategy(Object bean){
        if(isUniqueInterface(bean.getClass())){
            this.proxyStrategy = new JDKStrategy();
        }else{
            this.proxyStrategy = new ByteBuddyStrategy();
        }
    }
    //该类是否只有一个接口
    private Boolean isUniqueInterface(Class<?> clazz){
        Class<?>[] interfaces = clazz.getInterfaces();
        return interfaces.length == 1;
    }
}