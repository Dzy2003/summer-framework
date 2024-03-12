package com.duan.summer.resolve;

import com.duan.summer.annotations.PathVariable;
import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;

/**
 * @author 白日
 * @create 2024/3/10 21:25
 * @description
 */

public class PathVariableMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver{
    @Override
    protected Object resolveName(String parameterName, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) {
        String handlerMethodPath = handlerMethod.getPath();
        String requestPath = webServletRequest.request().
                getRequestURI().substring(webServletRequest.request().getContextPath().length());
        logger.debug("request path:{}", handlerMethodPath);
        logger.debug("real Path{}", requestPath);
        String parameterValue = getParameterValue(parameterName, handlerMethodPath, requestPath);
        return convent.convent(parameterValue);
    }

    private static String getParameterValue(String parameterName, String handlerMethodPath, String requestPath) {
        String[] handlerPaths = handlerMethodPath.split("/");
        String[] requestPaths = requestPath.split("/");
        String parameterValue = null;
        for (int i = 0; i < handlerPaths.length; i++) {
            String handlerPath = handlerPaths[i];
            if (handlerPath.startsWith("{") && handlerPath.endsWith("}")) {
                String pathVariableName = handlerPath.substring(1, handlerPath.length() - 1);
                if(pathVariableName.equals(parameterName)){
                    parameterValue = requestPaths[i];
                }
            }
        }
        return parameterValue;
    }

    @Override
    protected NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        PathVariable pathVariable = parameter.getParameterAnnotation(PathVariable.class);
        if (pathVariable == null) throw new RuntimeException("there is no PathVariable annotation in parameter in"
                + parameter.getParameterName());
        return new NamedValueInfo(pathVariable.value(), null, pathVariable.required());
    }

    @Override
    protected void resolveArgumentName() {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().isAnnotationPresent(PathVariable.class);
    }
}
