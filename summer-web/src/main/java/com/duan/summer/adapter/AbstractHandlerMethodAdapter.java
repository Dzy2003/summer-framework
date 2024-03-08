package com.duan.summer.adapter;

import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.web.ModelAndView;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 白日
 * @create 2024/3/6 9:53
 * @description
 */

public abstract class AbstractHandlerMethodAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Nullable
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.handleInternal(request, response, (HandlerMethod) handler);
    }

    protected abstract ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
                                                   HandlerMethod handler) throws Exception;
}
