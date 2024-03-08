package com.duan.summer.adapter;

import com.duan.summer.web.ModelAndView;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 白日
 * @create 2024/3/6 9:39
 * @description
 */

public interface HandlerAdapter {

    boolean supports(Object handler);
    @Nullable
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}
