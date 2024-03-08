package com.duan.summer.resolve;

import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/3/6 15:57
 * @description 责任链模式，找到一个能够处理参数的处理器
 */

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{
    final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    Map<MethodParameter,HandlerMethodArgumentResolver> argumentResolverCache = new HashMap<>();
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        for (HandlerMethodArgumentResolver resolver : resolvers) {
            if(resolver.supportsParameter(parameter)){
                argumentResolverCache.put(parameter, resolver);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) throws Exception {
        return argumentResolverCache.get(parameter).resolveArgument(parameter, handlerMethod, webServletRequest, convent);
    }
    public HandlerMethodArgumentResolverComposite addResolvers(List<HandlerMethodArgumentResolver> resolvers){
        this.resolvers.addAll(resolvers);
        return this;
    }
}
