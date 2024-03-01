package com.duan.summer.handler;

import com.duan.summer.annotations.Controller;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author 白日
 * @create 2024/2/28 13:17
 * @description
 */

public class RequestMappingHandlerMapping extends AbstractHandlerMapping{
    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) {
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        Map<String, Set<HandlerMethod>> matchingPath = this.mappingRegistry.getMatchingPath();
        Map<String, Set<HandlerMethod>> containsPathVariableMatchingPath =
                this.mappingRegistry.getContainsPathVariableMatchingPath();
        if(matchingPath.containsKey(requestPath)){
            List<HandlerMethod> matchHandlerMethods = getHandlerMethods(request, requestPath, matchingPath);
            if(matchHandlerMethods.size() == 1) return matchHandlerMethods.get(0);
            if(matchHandlerMethods.isEmpty()) throw new RuntimeException(requestPath+ "请求类型不匹配");
        }else {
            for (String path : containsPathVariableMatchingPath.keySet()) {
                if(fuzzyMatching(path, requestPath, containsPathVariableMatchingPath)){
                    List<HandlerMethod> matchHandlerMethods = getHandlerMethods(request, path, containsPathVariableMatchingPath);
                    if(matchHandlerMethods.size() == 1) return matchHandlerMethods.get(0);
                    if(matchHandlerMethods.isEmpty()) throw new RuntimeException(requestPath+ "请求类型不匹配");
                }
            }
        }
        return null;
    }

    private List<HandlerMethod> getHandlerMethods(HttpServletRequest request, String path, Map<String, Set<HandlerMethod>> containsPathVariableMatchingPath) {
        return containsPathVariableMatchingPath.get(path)
                .stream()
                .filter(handlerMethod -> selectRequestMethod(handlerMethod, request.getMethod()))
                .toList();
    }

    private Boolean fuzzyMatching(String fuzzyMatchingPath, String requestPath, Map<String, Set<HandlerMethod>> containsPathVariableMatchingPath) {
        return Pattern.compile(fuzzyMatchingPath).matcher(requestPath).matches();

    }

    private Boolean selectRequestMethod(HandlerMethod handlerMethod, String requestMethod){
        return handlerMethod.getRequestMethod().name().equals(requestMethod);
    }

    @Override
    protected HandlerMethod getMappingForMethod(Method method, Object handler) {
        return new HandlerMethod(handler, method);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return beanType != null && beanType.isAnnotationPresent(Controller.class);
    }

}
