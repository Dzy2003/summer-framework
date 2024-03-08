package com.duan.summer.web;

import com.duan.summer.adapter.HandlerAdapter;
import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.ApplicationContextImpl;
import com.duan.summer.handler.HandlerExecutionChain;
import com.duan.summer.handler.HandlerMapping;
import com.duan.summer.handler.HandlerMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("{} {}", req.getMethod(), req.getRequestURI());
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, world!</h1>");
//        System.out.println(req.getRequestURI().substring(req.getContextPath().length()));
//        System.out.println(handlerMapping.getMappingRegistry());
        HandlerExecutionChain handler = this.handlerMapping.getHandler(req);
        HandlerMethod handlerMethod = handler.getHandlerMethod();
        try {
            handlerAdapter.handle(req, resp, handlerMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        pw.flush();
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
}
