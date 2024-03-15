package com.duan.summer.resolve;

import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;

/**
 * @author 白日
 * @create 2024/3/15 11:47
 * @description
 */

public class ModelAndViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportsReturnType(Class<?> returnType) {
        return ModelAndView.class.isAssignableFrom(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, Class<?> returnType, ModelAndView mav, WebServletRequest webRequest) throws Exception {
        ModelAndView modelAndView = (ModelAndView) returnValue;
        mav.setView(modelAndView.getView());
        mav.setRest(modelAndView.isRest());
        mav.setViewName(modelAndView.getViewName());
        mav.setModelMap(modelAndView.getModelMap());
        mav.isRedirect(modelAndView.getViewName().startsWith("redirect:"));
    }
}
