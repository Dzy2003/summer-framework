package com.duan.summer.web;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.ApplicationContext;
import com.duan.summer.exception.NestedRuntimeException;
import com.duan.summer.utils.WebApplicationContextUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 白日
 * @create 2024/2/20 22:06
 * @description
 */

public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
    final Logger logger = LoggerFactory.getLogger(getClass());
    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        super(context);
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("init {}.", getClass().getName());
        this.initApplicationContext(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.closeWebApplicationContext(sce.getServletContext());
    }

}
