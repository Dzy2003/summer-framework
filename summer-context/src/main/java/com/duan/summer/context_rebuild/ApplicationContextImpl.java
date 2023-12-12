package com.duan.summer.context_rebuild;

import com.duan.summer.context.BeanDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 白日
 * @create 2023/12/12 19:22
 * @description
 */

public class ApplicationContextImpl implements ApplicationContext{
    public final Map<String, BeanDefinition> beans = new HashMap<>();
    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public <T> T getBean(String name) {
        return null;
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
}
