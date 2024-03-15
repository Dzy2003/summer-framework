package com.duan.summer.web;

import com.duan.summer.utils.WebApplicationContextUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 白日
 * @create 2024/2/20 21:55
 * @description
 */

public abstract class BaseHttpServlet  extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    WebApplicationContext context;
    public BaseHttpServlet(WebApplicationContext webApplicationContext) {
        this.context = webApplicationContext;
    }
    @Override
    public void destroy() {
        this.context.close();
    }

    @Override
    public void init() {
        logger.info("加载成功");
        this.context = initServletBean();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("{} {}", req.getMethod(), req.getRequestURI());
        try {
            this.doService(req,resp);
        } catch (Exception e) {
            throw new RuntimeException("request" + req.getRequestURI() + " error", e);
        }
    }

    protected abstract void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    protected final WebApplicationContext initServletBean() {
        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        WebApplicationContext wac = null;
        if(this.context != null){
            wac = this.context;
            if(wac instanceof AnnotationConfigWebApplicationContext acwa){
                if(acwa.getParent() == null){
                    acwa.setParent(rootContext);
                }
                acwa.refresh();
            }
        }
        onRefresh(context);
        return wac;
    }

    protected abstract void onRefresh(WebApplicationContext webApplicationContext);

    public ServletContext getServletContext() {
        return this.getServletConfig().getServletContext();
    }
}
