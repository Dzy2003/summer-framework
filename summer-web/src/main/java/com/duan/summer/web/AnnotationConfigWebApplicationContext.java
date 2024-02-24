package com.duan.summer.web;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.exception.NoSuchBeanDefinitionException;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 白日
 * @create 2024/2/24 0:02`
 * @description
 */

public class AnnotationConfigWebApplicationContext extends AnnotationConfigApplicationContext implements WebApplicationContext {
    private ServletContext servletContext;
    private WebApplicationContext parent;

    public AnnotationConfigWebApplicationContext(){

    }
    public void addComponentClasses(Class<?>... componentClasses){
        this.componentClasses = componentClasses;
    }

    public AnnotationConfigWebApplicationContext(Class<?>... componentClasses){
        super(componentClasses);
    }
    public void setParent(WebApplicationContext parent) {
        this.parent = parent;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Nullable
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }
    public WebApplicationContext getParent() {
        return this.parent;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        T bean = null;
        try {
            if (parent != null) bean = parent.getBean(requiredType);
        }catch (NoSuchBeanDefinitionException ignored){

        }
        if(bean == null) bean = super.getBean(requiredType);
        return bean;
    }

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        List<T> beans = null;
        try {
            if (parent != null) return parent.getBeans(requiredType);
        }catch (Exception e){
            beans = super.getBeans(requiredType);
        }
        return beans;
    }
}
