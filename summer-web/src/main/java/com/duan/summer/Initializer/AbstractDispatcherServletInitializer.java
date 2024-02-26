package com.duan.summer.Initializer;

import com.duan.summer.web.DispatcherServlet;
import com.duan.summer.web.WebApplicationContext;
import jakarta.servlet.*;

import java.util.EnumSet;

/**
 * @author 白日
 * @create 2024/2/25 13:43
 * @description
 */

public abstract class AbstractDispatcherServletInitializer extends AbstractContextLoaderInitializer{
    public static final String DEFAULT_SERVLET_NAME = "dispatcher";
    public void onStartup(ServletContext servletContext) {
        super.onStartup(servletContext);
        this.registerDispatcherServlet(servletContext);
    }

    protected void registerDispatcherServlet(ServletContext servletContext) {
        WebApplicationContext servletApplicationContext = this.createServletApplicationContext();
        if(servletApplicationContext == null) logger.debug("createServletApplicationContext() must not return null");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(servletApplicationContext);
        ServletRegistration.Dynamic registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if(registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME +
                    "'. Check if there is another servlet registered under the same name.");
        }else {
            registration.setLoadOnStartup(1);//初始化优先级
            registration.addMapping(this.getServletMappings());//servlet处理映射
            registration.setAsyncSupported(this.isAsyncSupported());//异步处理支持
            //注册过滤器
            Filter[] filters = this.getServletFilters();
            if (filters != null) {
                for (Filter filter : filters) {
                    registerServletFilter(servletContext, filter);
                }
            }
            //用户自定义抽象方法
            this.customizeRegistration(registration);
        }
    }

    /**
     * 定制DispatcherServlet配置
     * @param registration 注册DispatcherServlet信息
     */
    protected  void customizeRegistration(ServletRegistration.Dynamic registration){

    }

    /**
     * 用户添加过滤器
     * @return 过滤器
     */
    protected Filter[] getServletFilters() {
        return null;
    }

    /**
     * 异步支持
     * @return boolean
     */
    protected boolean isAsyncSupported() {
        return true;
    }

    /**
     * 配置DispatcherServletMapping信息
     * @return String[]
     */
    protected abstract String[] getServletMappings();

    /**
     * 创建WebApplicationContext子容器
     * @return 子容器
     */
    protected abstract WebApplicationContext createServletApplicationContext();
    private FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        String filterName = filter.getClass().getSimpleName();
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            for(int counter = 0; registration == null; ++counter) {
                if (counter == 100) {
                    throw new IllegalStateException("Failed to register filter with name '" + filterName + "'. Check if there is another filter registered under the same name.");
                }
                registration = servletContext.addFilter(filterName + "#" + counter, filter);
            }
        }

        registration.setAsyncSupported(this.isAsyncSupported());
        registration.addMappingForServletNames(this.getDispatcherTypes(),
                false, new String[]{DEFAULT_SERVLET_NAME});
        return registration;
    }
    private EnumSet<DispatcherType> getDispatcherTypes() {
        return this.isAsyncSupported() ? EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC) : EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
    }

}
