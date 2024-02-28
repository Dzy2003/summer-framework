package com.duan.summer.handler;

import com.duan.summer.annotations.Controller;
import com.duan.summer.web.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

/**
 * @author 白日
 * @create 2024/2/28 13:17
 * @description
 */

public class RequestMappingHandlerMapping extends AbstractHandlerMapping{
    @Override
    protected HandlerMethod getMappingForMethod(Method method, Object handler) {
        return new HandlerMethod(handler, method);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return beanType != null && beanType.isAnnotationPresent(Controller.class);
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        return null;
    }
}
