package com.duan.summer.resolve;

import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;

/**
 * @author 白日
 * @create 2024/3/15 12:06
 * @description
 */

public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportsReturnType(Class<?> returnType) {
        return String.class.isAssignableFrom(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, Class<?> returnType, ModelAndView modelAndView, WebServletRequest webRequest) throws Exception {
        String viewName = returnValue.toString();
        modelAndView.setViewName(viewName);
        modelAndView.isRedirect(viewName.startsWith("redirect:"));
    }
}
