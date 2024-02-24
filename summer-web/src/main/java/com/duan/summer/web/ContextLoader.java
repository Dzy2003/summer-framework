package com.duan.summer.web;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.ApplicationContext;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;

/**
 * @author 白日
 * @create 2024/2/23 23:44
 * @description
 */

public class ContextLoader {
    @Nullable
    private WebApplicationContext context;
    public ContextLoader() {
    }

    public ContextLoader(WebApplicationContext context) {
        this.context = context;
    }

    public WebApplicationContext initApplicationContext(ServletContext servletContext) {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        return this.context;
    }

    public void closeWebApplicationContext(ServletContext servletContext){
        assert this.context != null;
        this.context.close();
        servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }


}
