package com.duan.summer.context_rebuild;

import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/4 18:08
 * @description
 */

public interface ApplicationContextAware {
    public void setApplicationContext(Map<String, BeanDefinition> beans);
}
