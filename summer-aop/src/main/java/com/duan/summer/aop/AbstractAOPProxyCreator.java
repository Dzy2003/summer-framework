package com.duan.summer.aop;

import com.duan.summer.annotation.Around;
import com.duan.summer.annotation.Aspect;
import com.duan.summer.annotation.Component;
import com.duan.summer.context_rebuild.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 白日
 * @create 2024/1/4 18:00
 * @description
 */
@Component
public class AbstractAOPProxyCreator implements BeanPostProcessor, ApplicationContextAware {
    Map<String, Object> originBeans = new HashMap<>();
    public Map<String, BeanDefinition> beans;
    public final Map<String, List<Advice>> proxyRule = new ConcurrentHashMap<>();
    public List<Object> aspectInstance = new ArrayList<>(8);
    ProxyResolver proxyResolver = new ProxyResolver();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if(checkJoinPoint(beanClass)){
            originBeans.put(beanName, bean);
            Object proxy = proxyResolver.createProxy(bean, new DynamicAopProxy(proxyRule, bean));
            System.out.println(proxy.getClass().equals(beanClass));
            return proxy;
        }
        return bean;

    }


    private boolean checkJoinPoint(Class<?> beanClass) {
        for (Method method : beanClass.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if(proxyRule.containsKey(annotation.annotationType().getSimpleName())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object postProcessOnSetProperty(Object bean, String beanName) {
        Object origin = this.originBeans.get(beanName);
        return origin != null ? origin : bean;
    }

    @Override
    public void setApplicationContext(Map<String, BeanDefinition> beans) {
        this.beans = beans;
        aspectInstance = getAspectInstance();
        parseAspectjClass();
    }

    private void parseAspectjClass(){
        for (Object aspect : aspectInstance) {
            for (Method method : aspect.getClass().getMethods()) {
                Around around = method.getAnnotation(Around.class);
                if(around != null){
                    Advice advice = new Advice(method, aspect);
                    String targetAnnoName = around.targetAnno().getSimpleName();
                    if(proxyRule.containsKey(targetAnnoName)){
                        proxyRule.get(targetAnnoName).add(advice);
                    }else {
                        List<Advice> proxyChains = new ArrayList<>();
                        proxyChains.add(advice);
                        proxyRule.put(targetAnnoName, proxyChains);
                    }
                }
            }
        }
    }
    public List<Object> getAspectInstance() {
        List<BeanDefinition> aspectDef = beans.values().
                stream()
                .filter(definition -> definition.getBeanClass().isAnnotationPresent(Aspect.class))
                .toList();
        return aspectDef.stream().map(BeanDefinition::getInstance).toList();
    }
}
