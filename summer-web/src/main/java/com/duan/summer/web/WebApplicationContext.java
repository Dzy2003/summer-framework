package com.duan.summer.web;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.ApplicationContext;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;

/**
 * @author 白日
 * @create 2024/2/23 23:52
 * @description
 */

public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
    String CHILD_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".CHILD";
    @Nullable
    ServletContext getServletContext();
}
