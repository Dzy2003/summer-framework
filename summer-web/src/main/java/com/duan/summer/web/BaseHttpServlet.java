package com.duan.summer.web;

import com.duan.summer.utils.WebApplicationContextUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
