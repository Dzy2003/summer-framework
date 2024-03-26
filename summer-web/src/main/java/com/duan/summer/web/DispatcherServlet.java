package com.duan.summer.web;

import com.duan.summer.adapter.HandlerAdapter;
import com.duan.summer.handler.HandlerExecutionChain;
import com.duan.summer.handler.HandlerMapping;
import com.duan.summer.handler.HandlerMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 白日
 * @create 2024/2/25 13:09
 * @description
 */

public class DispatcherServlet extends BaseHttpServlet{
    protected HandlerMapping handlerMapping;

    protected HandlerAdapter handlerAdapter;
    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecutionChain handler = handlerMapping.getHandler(req);
        //logger.info(this.handlerMapping.getMappingRegistry().toString());
        if(handler == null){
            this.noHandlerFound(req, resp);
            return;
        }
        HandlerAdapter handlerAdapter = this.getHandlerAdapter(handler.getHandlerMethod());
        ModelAndView mv = handlerAdapter.handle(req, resp, handler.getHandlerMethod());
        if(mv.isRest()) return;
        System.out.println(mv);



    }

    private HandlerAdapter getHandlerAdapter(HandlerMethod handler) throws ServletException {
        if(this.handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");

    }

    @Override
    protected void onRefresh(WebApplicationContext webApplicationContext) {
        this.initStrategies(webApplicationContext);
    }


    private void initStrategies(WebApplicationContext webApplicationContext) {
        this.initHandlerMapping(webApplicationContext);
        this.initHandlerAdapter(webApplicationContext);
    }

    private void initHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.handlerAdapter = webApplicationContext.getBean(HandlerAdapter.class);
    }

    private void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = webApplicationContext.getBean(HandlerMapping.class);
    }

    private void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.warn("No mapping for " + req.getMethod() + " " + req.getRequestURI());
        resp.sendError(404);
    }
}
