package com.duan.summer.handler;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 白日
 * @create 2024/2/27 12:36
 * @description
 */

public interface HandlerMapping {
    HandlerExecutionChain getHandler(HttpServletRequest request);

    AbstractHandlerMapping.MappingRegistry getMappingRegistry();

}
