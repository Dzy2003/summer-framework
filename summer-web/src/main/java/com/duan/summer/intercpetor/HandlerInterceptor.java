package com.duan.summer.intercpetor;

import com.duan.summer.handler.HandlerMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 白日
 * @create 2024/2/27 12:56
 * @description
 */

public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response){
        return true;
    }

    default void  postHandle(HttpServletRequest request, HttpServletResponse response){}

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler,
                                 Exception ex){
    }
}
