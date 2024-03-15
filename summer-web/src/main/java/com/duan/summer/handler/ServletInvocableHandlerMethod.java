package com.duan.summer.handler;

import com.duan.summer.convert.Convent;
import com.duan.summer.resolve.HandlerMethodReturnValueHandlerComposite;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/3/6 16:54
 * @description
 */

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod{
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
    public ServletInvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public void invokeAndHandle(WebServletRequest servletRequest, ModelAndView modelAndView, Map<Class<?>, Convent<?>> conventMap) throws Exception{
        Object returnValue = this.invokeForRequest(servletRequest, modelAndView,conventMap); //调用方法
        if(returnValue == null) return;
        returnValueHandlers.handleReturnValue(returnValue, returnValue.getClass(), modelAndView, servletRequest); //处理返回值
    }

    public void setReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }
}
