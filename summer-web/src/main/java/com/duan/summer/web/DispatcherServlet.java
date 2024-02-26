package com.duan.summer.web;

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
        pw.flush();
    }
}
