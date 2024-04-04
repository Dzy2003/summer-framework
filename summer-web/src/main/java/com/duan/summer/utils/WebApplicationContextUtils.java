package com.duan.summer.utils;


import com.duan.summer.web.WebApplicationContext;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 白日
 * @create 2024/2/20 21:52
 * @description
 */

public class WebApplicationContextUtils {
    static final Logger logger = LoggerFactory.getLogger(WebApplicationContextUtils.class);


    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        return (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

//    public static void registerDispatcherServlet(ServletContext servletContext) {
//        var dispatcherServlet = new DispatcherServlet(ApplicationContextUtils.getRequiredApplicationContext());
//        logger.info("register servlet {} for URL '/'", dispatcherServlet.getClass().getName());
//        var dispatcherReg = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
//        dispatcherReg.addMapping("/");
//        dispatcherReg.setLoadOnStartup(0);
//    }
}
