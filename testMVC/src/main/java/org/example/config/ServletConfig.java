package org.example.config;


import com.duan.summer.Initializer.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 创建Web项目入口配置类
 * 1.加载spring配置类
 * 2.加载springMVC配置类
 * 3.设置springMVC地址拦截规则
 */

public class ServletConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
