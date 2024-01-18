package com.duan.summer.context;

import com.duan.summer.exception.BeansException;

/**
 * @author 白日
 * @create 2024/1/18 21:23
 * @description
 */

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanDefinitionRegistry definitions) throws BeansException;
}
