package com.duan.summer.Initializer;

import com.duan.summer.web.AnnotationConfigWebApplicationContext;
import com.duan.summer.web.WebApplicationContext;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletRegistration;

/**
 * @author 白日
 * @create 2024/2/25 14:07
 * @description
 */

public abstract class AbstractAnnotationConfigDispatcherServletInitializer extends AbstractDispatcherServletInitializer{
    @Nullable
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        Class<?>[] configClasses = this.getRootConfigClasses();
        if(configClasses != null && configClasses.length != 0){
            return new AnnotationConfigWebApplicationContext(configClasses);
        }else {
            return null;
        }
    }


    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        Class<?>[] configClasses = this.getServletConfigClasses();
        if(configClasses != null && configClasses.length != 0){
            context.addComponentClasses(configClasses);
        }
        return context;
    }

    @Nullable
    protected abstract Class<?>[] getRootConfigClasses();

    @Nullable
    protected abstract Class<?>[] getServletConfigClasses();
}
