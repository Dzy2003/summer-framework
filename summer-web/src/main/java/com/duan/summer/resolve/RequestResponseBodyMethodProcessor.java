package com.duan.summer.resolve;

import com.duan.summer.annotations.RequestBody;
import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 白日
 * @create 2024/3/12 19:58
 * @description
 */

public class RequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestBody.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        if(parameter.isList()) {
            parameterType = parameter.getGenericClass();
        }
        return this.readArgFromRequestBody(webServletRequest.request(),parameterType, parameter);
    }

    @Override
    public boolean supportsReturnType(Class<?> returnType) {
        return !(returnType.isPrimitive() || returnType.isAssignableFrom(String.class) ||
                ModelAndView.class.isAssignableFrom(returnType));
    }

    @Override
    public void handleReturnValue(Object returnValue, Class<?> returnType, ModelAndView modelAndView, WebServletRequest webRequest) throws Exception {
        final HttpServletResponse response = webRequest.response();
        modelAndView.setRest(true);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(returnValue));
        response.getWriter().flush();
    }
}
