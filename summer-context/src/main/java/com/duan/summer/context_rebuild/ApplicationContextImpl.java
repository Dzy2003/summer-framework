package com.duan.summer.context_rebuild;

import com.duan.summer.annotation.Configuration;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.utils.ClassUtils;

import java.util.*;

/**
 * @author 白日
 * @create 2023/12/12 19:22
 * @description
 */

public abstract class ApplicationContextImpl implements ApplicationContext{
    public final Map<String, BeanDefinition> beans;
    Set<String> creatingBeanNames;
    public ApplicationContextImpl(){
        beans = new HashMap<>();
        creatingBeanNames = new HashSet<>();
    }
    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public <T> T getBean(String name) {
        return (T) beans.get(name).getInstance();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        return null;
    }

    @Override
    public void close() {

    }

    protected void createBean(){
        createConfigurationBean();
        createCommonBean();
    }
    private void createConfigurationBean(){
        beans.values()
                .stream()
                .filter(this::isConfigurationDefinition)
                .sorted()
                .forEach(this::createBeanAsEarlySingleton);

    }
    private void createCommonBean(){
        List<BeanDefinition> beanDefinitions = beans.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.getInstance() == null)
                .toList();
        beanDefinitions.forEach(beanDefinition -> {
            if(beanDefinition.getInstance() == null) createBeanAsEarlySingleton(beanDefinition);
        });
    }
    protected abstract Object createBeanAsEarlySingleton(BeanDefinition definition);

    protected boolean isConfigurationDefinition(BeanDefinition def) {
        return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;
    }
}
