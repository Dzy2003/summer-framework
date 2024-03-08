package com.duan.summer.resolve;

import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;

/**
 * @author 白日
 * @create 2024/3/6 10:35
 * @description 处理handler方法参数
 */

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent) throws Exception;
}
