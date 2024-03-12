package com.duan.summer.resolve;

import com.duan.summer.annotations.RequestParam;
import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;

import java.lang.invoke.MethodHandle;

/**
 * @author 白日
 * @create 2024/3/8 19:16
 * @description
 */

public class RequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver{
    @Override
    protected Object resolveName(String parameterName, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) {
        Object arg = null;
        String[] paramValues = webServletRequest.request().getParameterValues(parameterName);
        if (paramValues != null) {
            arg = paramValues.length == 1 ? paramValues[0] : paramValues;
        }
        if(arg == null) return null;
        return convent.convent(arg);
    }

    @Override
    protected NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        RequestParam requestParam = parameter.getParameter().getAnnotation(RequestParam.class);
        String name = requestParam != null ? requestParam.value() : parameter.getParameterName();
        return new NamedValueInfo(name, requestParam.defaultValue(), requestParam.Required());
    }

    @Override
    protected void resolveArgumentName() {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().isAnnotationPresent(RequestParam.class);
    }
}
