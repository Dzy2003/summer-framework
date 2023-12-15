package com.duan.summer.context_rebuild;

import com.duan.summer.context.BeanDefinition;

import java.util.Properties;
import java.util.Set;

/**
 * @author 白日
 * @create 2023/12/12 16:30
 * @description 注册BeanDefinition的接口
 */

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    BeanDefinition findBeanDefinition(String beanName);
    BeanDefinition findBeanDefinition(Class<?> type);
    BeanDefinition findBeanDefinition(String beanName,Class<?> type);
    void removeBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
    Set<String> getBeanDefinitionNames();

    void registryPropertyResolver(Properties properties);

}
