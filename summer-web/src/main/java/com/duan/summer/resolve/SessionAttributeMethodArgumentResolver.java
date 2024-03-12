package com.duan.summer.resolve;

import com.duan.summer.annotations.SessionAttribute;
import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @author 白日
 * @create 2024/3/11 17:15
 * @description
 */

public class SessionAttributeMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver{
    @Override
    protected Object resolveName(String parameterName, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) {
        HttpSession session = webServletRequest.request().getSession();
        if(session != null) {
            return convent.convent(session.getAttribute(parameterName));
        }
        return null;
    }

    @Override
    protected NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        SessionAttribute sessionAttribute = parameter.getParameterAnnotation(SessionAttribute.class);
        return new NamedValueInfo(sessionAttribute.value(), null, sessionAttribute.required());
    }

    @Override
    protected void resolveArgumentName() {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SessionAttribute.class) != null;
    }
}
