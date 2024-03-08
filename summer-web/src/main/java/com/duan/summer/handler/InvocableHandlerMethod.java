package com.duan.summer.handler;

import com.duan.summer.convert.Convent;
import com.duan.summer.resolve.HandlerMethodArgumentResolverComposite;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/3/6 10:27
 * @description
 */

public class InvocableHandlerMethod extends HandlerMethod{

    HandlerMethodArgumentResolverComposite methodArgumentResolver;
    public InvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }
    public InvocableHandlerMethod(HandlerMethod handlerMethod){
        super(handlerMethod);
    }

    protected Object invokeForRequest(WebServletRequest servletRequest, ModelAndView modelAndView, Map<Class<?>, Convent<?>> conventMap) throws Exception{
        Object[] args = this.getMethodArgumentValues(servletRequest, modelAndView, conventMap);
        return this.getMethod().invoke(this.getHandler(), args);
    }

    private Object[] getMethodArgumentValues(WebServletRequest servletRequest, ModelAndView modelAndView, Map<Class<?>, Convent<?>> conventMap) {
        //TODO 解析方法上的参数值
        MethodParameter[] parameters = this.getParameters();
        if(parameters == null || parameters.length == 0) return new Object[0];
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            if(!methodArgumentResolver.supportsParameter(parameter)){
                throw new RuntimeException("no suitable resolver for " + parameter);
            }
            try {
                Convent<?> convent;
                if(parameter.isList){
                    convent = conventMap.get(parameter.getGenericClass());
                }else {
                    convent = conventMap.get(parameter.getParameterType());
                }
                args[i] = methodArgumentResolver.resolveArgument(parameter, this, servletRequest,convent);
            } catch (Exception e) {
                throw new RuntimeException("方法参数解析失败",e);
            }
        }
        return args;
    }

    public void setMethodArgumentResolver(HandlerMethodArgumentResolverComposite methodArgumentResolver) {
        this.methodArgumentResolver = methodArgumentResolver;
    }
}
