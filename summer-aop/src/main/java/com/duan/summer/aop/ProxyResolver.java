package com.duan.summer.aop;
import java.lang.reflect.InvocationHandler;

/**
 * @author 白日
 * @create 2024/1/1 16:06
 * @description
 */

public class ProxyResolver {
    GeneratorProxyStrategy proxyStrategy;


    public Object createProxy(Object bean, InvocationHandler handler){
        chooseStrategy(bean);
        return proxyStrategy.createProxy(bean, handler);
    }

    private void chooseStrategy(Object bean){
        if(isUniqueInterface(bean.getClass())){
            this.proxyStrategy = new JDKStrategy();
        }else{
            this.proxyStrategy = new ByteBuddyStrategy();
        }
    }
    private Boolean isUniqueInterface(Class<?> clazz){
        Class<?>[] interfaces = clazz.getInterfaces();
        return interfaces.length == 1;
    }

}
