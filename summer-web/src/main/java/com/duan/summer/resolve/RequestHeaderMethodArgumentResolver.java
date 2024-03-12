package com.duan.summer.resolve;

import com.duan.summer.annotations.RequestHeader;
import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;

/**
 * @author 白日
 * @create 2024/3/11 17:05
 * @description
 */

public class RequestHeaderMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver{
    @Override
    protected Object resolveName(String parameterName, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) {
        Object arg = null;
        arg = webServletRequest.request().getHeader(parameterName);
        if(arg == null) throw new RuntimeException("request header is not contains key" + parameterName);
        return convent.convent(arg);
    }

    @Override
    protected NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        RequestHeader requestHeader = parameter.getParameterAnnotation(RequestHeader.class);
        return new NamedValueInfo(requestHeader.value(), null, requestHeader.require());
    }

    @Override
    protected void resolveArgumentName() {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestHeader.class) != null;
    }
}
