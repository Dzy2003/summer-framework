package com.duan.summer.context;

import com.duan.summer.annotations.Component;
import com.duan.summer.exception.BeansException;
import com.duan.summer.io.ResourceResolver;

import java.util.List;

/**
 * @author 白日
 * @create 2024/1/24 0:13
 * @description
 */

public class BeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;

    public BeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }
    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }

    private List<BeanDefinition> doScan(String basePackage) {
        List<Class<?>> beanDefinitionClasses = new ResourceResolver(basePackage).scan(resource -> {
            if (resource.name().endsWith(".class")) {
                try {
                    return Class.forName(resource.name().substring(0, resource.name().length() - 6).replace("\\","."));
                } catch (ClassNotFoundException e) {
                    throw new BeansException(e);
                }
            }
            return null;
        });
        List<BeanDefinition> beanDefinitions = beanDefinitionClasses.stream()
                .filter(this::isComponent)
                .map(BeanDefinitionFactory::createBeanDefinition)
                .toList();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registry.registerBeanDefinition(beanDefinition.getName(),beanDefinition);
        }
        return beanDefinitions;
    }

    private boolean isComponent(Class<?> aClass) {
        return aClass.isAnnotationPresent(Component.class);
    }
}
