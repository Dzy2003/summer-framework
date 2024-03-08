package com.duan.summer.resolve;

import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/3/6 16:26
 * @description 责任链模式，找到一个能够处理返回值的处理器
 */

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler{
    List<HandlerMethodReturnValueHandler> resolvers = new ArrayList<>();
    @Override
    public boolean supportsReturnType(Class<?> returnType) {
        return getReturnValueHandler(returnType) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, Class<?> returnType, ModelAndView modelAndView, WebServletRequest webRequest) throws Exception {
        getReturnValueHandler(returnType).handleReturnValue(returnValue, returnType, modelAndView, webRequest);
    }

    private HandlerMethodReturnValueHandler getReturnValueHandler(Class<?> returnType){
        HandlerMethodReturnValueHandler returnValueHandler = null;
        for (HandlerMethodReturnValueHandler resolver : resolvers) {
            if(resolver.supportsReturnType(returnType)){
                returnValueHandler = resolver;
            }
        }
        return returnValueHandler;
    }
    public HandlerMethodReturnValueHandlerComposite addResolvers(List<HandlerMethodReturnValueHandler> resolvers){
        this.resolvers.addAll(resolvers);
        return this;
    }
}
