package com.duan.summer.context;

import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/4 18:08
 * @description
 */

public interface BeansAware {
    public void setApplicationContext(Map<String, BeanDefinition> beans);
}
