package com.duan.summer.aop;

import com.duan.summer.annotation.Around;
import com.duan.summer.annotation.Aspect;
import com.duan.summer.annotation.Component;
import com.duan.summer.context_rebuild.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AOPProxyCreator implements BeanPostProcessor, ApplicationContextAware {
    Map<String, Object> originBeans = new HashMap<>();
    public Map<String, BeanDefinition> beans;
    public final Map<Class<? extends Annotation>, List<Advice>> proxyRule = new ConcurrentHashMap<>();
    public List<Object> aspectInstance = new ArrayList<>(8);
    ProxyFactory proxyResolver = new ProxyFactory();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if(checkJoinPoint(beanClass)){
            originBeans.put(beanName, bean);
            return proxyResolver.createProxy(bean, new DynamicAopProxy(proxyRule, bean));
        }
        return bean;

    }


    private boolean checkJoinPoint(Class<?> beanClass) {
        for (Annotation annotation : beanClass.getAnnotations()) {
            if(proxyRule.containsKey(annotation.annotationType())){
                return true;
            }
        }
        for (Method method : beanClass.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if(proxyRule.containsKey(annotation.annotationType())){
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
        logger.debug("解析后的拦截规则为:{}", proxyRule);
    }

    private void parseAspectjClass(){
        for (Object aspect : aspectInstance) {
            for (Method method : aspect.getClass().getMethods()) {
                Around around = method.getAnnotation(Around.class);
                if(around != null){
                    Advice advice = new Advice(method, aspect);
                    Class<? extends Annotation> targetAnno = around.targetAnno();
                    if(proxyRule.containsKey(targetAnno)){
                        proxyRule.get(targetAnno).add(advice);
                    }else {
                        List<Advice> proxyChains = new ArrayList<>();
                        proxyChains.add(advice);
                        proxyRule.put(targetAnno, proxyChains);
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
