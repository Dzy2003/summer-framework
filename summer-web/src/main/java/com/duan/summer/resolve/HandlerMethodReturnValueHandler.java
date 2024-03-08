package com.duan.summer.resolve;

import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;

/**
 * @author 白日
 * @create 2024/3/6 10:36
 * @description
 */

public interface HandlerMethodReturnValueHandler {
    boolean supportsReturnType(Class<?> returnType);
    void handleReturnValue(Object returnValue, Class<?> returnType,
                           ModelAndView modelAndView, WebServletRequest webRequest) throws Exception;
}
