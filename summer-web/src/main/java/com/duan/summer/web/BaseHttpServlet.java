package com.duan.summer.web;

import com.duan.summer.utils.WebApplicationContextUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 白日
 * @create 2024/2/20 21:55
 * @description
 */

public class BaseHttpServlet  extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    WebApplicationContext webApplicationContext;
    public BaseHttpServlet(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
    @Override
    public void destroy() {
        this.webApplicationContext.close();
    }

    @Override
    public void init() {
        logger.info("加载成功");
        this.webApplicationContext = initServletBean();
    }

    protected final WebApplicationContext initServletBean() {
        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        WebApplicationContext wac = null;
        if(this.webApplicationContext != null){
            wac = this.webApplicationContext;
            if(wac instanceof AnnotationConfigWebApplicationContext acwa){
                if(acwa.getParent() == null){
                    acwa.setParent(rootContext);
                }
                acwa.refresh();
            }
        }
        return wac;
    }
    public ServletContext getServletContext() {
        return this.getServletConfig().getServletContext();
    }
}
