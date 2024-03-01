package com.duan.summer.handler;

/**
 * @author 白日
 * @create 2024/2/27 12:37
 * @description
 */

public class HandlerExecutionChain {

    private HandlerMethod handlerMethod;
    public HandlerExecutionChain(HandlerMethod handlerMethod){
        this.handlerMethod = handlerMethod;
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }
}
