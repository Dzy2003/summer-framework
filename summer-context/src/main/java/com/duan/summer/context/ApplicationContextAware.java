package com.duan.summer.context;

/**
 * @author 白日
 * @create 2024/2/28 13:52
 * @description
 */

public interface ApplicationContextAware extends Aware{
    void setApplication(ApplicationContext application);
}
