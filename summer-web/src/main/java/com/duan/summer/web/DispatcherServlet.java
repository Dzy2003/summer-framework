package com.duan.summer.web;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.ApplicationContextImpl;
import com.duan.summer.handler.HandlerMapping;
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
    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public void destroy() {
        super.destroy();
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("{} {}", req.getMethod(), req.getRequestURI());
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, world!</h1>");
        HandlerMapping handlerMapping = context.getBean(HandlerMapping.class);
        System.out.println(handlerMapping.getMappingRegistry());
        System.out.println();
        //System.out.println(handlerMapping.getMappingRegistry());
        pw.flush();
    }

    @Override
    protected void onRefresh(WebApplicationContext webApplicationContext) {
        this.initStrategies(webApplicationContext);
    }

    private void initStrategies(WebApplicationContext webApplicationContext) {
        this.initHandlerMapping(webApplicationContext);
    }

    private void initHandlerMapping(WebApplicationContext webApplicationContext) {
        this.handlerMapping = webApplicationContext.getBean(HandlerMapping.class);

    }
}
