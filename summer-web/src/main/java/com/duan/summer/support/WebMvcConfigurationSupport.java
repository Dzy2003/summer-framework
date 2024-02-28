package com.duan.summer.support;

import com.duan.summer.annotations.Bean;
import com.duan.summer.handler.HandlerMapping;
import com.duan.summer.handler.RequestMappingHandlerMapping;
import com.duan.summer.intercpetor.HandlerInterceptor;

import java.util.List;

/**
 * @author 白日
 * @create 2024/2/28 13:45
 * @description 配置MVC组件
 */

public abstract class WebMvcConfigurationSupport {
    List<HandlerInterceptor> interceptors;

    @Bean(initMethod = "afterPropertySet")
    public HandlerMapping handlerMapping(){
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        return requestMappingHandlerMapping;
    }



}
