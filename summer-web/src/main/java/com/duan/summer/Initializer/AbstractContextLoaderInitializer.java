package com.duan.summer.Initializer;

import com.duan.summer.web.ContextLoaderListener;
import com.duan.summer.web.WebApplicationContext;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 白日
 * @create 2024/2/25 13:38
 * @description
 */

public abstract class AbstractContextLoaderInitializer implements WebApplicationInitializer{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onStartup(ServletContext servletContext) {
        this.registerContextLoaderListener(servletContext);
    }

    protected void registerContextLoaderListener(ServletContext servletContext) {
        WebApplicationContext rootAppContext = this.createRootApplicationContext();
        if(rootAppContext != null){
            ContextLoaderListener contextLoaderListener = new ContextLoaderListener(rootAppContext);
            servletContext.addListener(contextLoaderListener);
        }else {
            this.logger.debug
                    ("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
        }
    }

    @Nullable
    protected abstract WebApplicationContext createRootApplicationContext();
}
