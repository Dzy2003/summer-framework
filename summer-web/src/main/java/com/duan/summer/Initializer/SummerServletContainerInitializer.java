package com.duan.summer.Initializer;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author 白日
 * @create 2024/2/25 13:15
 * @description
 */
//注解指定初始化类的接口
@HandlesTypes({WebApplicationInitializer.class})
public class SummerServletContainerInitializer implements ServletContainerInitializer{
    public SummerServletContainerInitializer() {
    }
    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList<>();
        if(webAppInitializerClasses != null){
            initializers = webAppInitializerClasses
                    .stream().
                    filter(this::checkImplements)
                    .map(this::getInitializerInstance)
                    .toList();
        }
        if(initializers.isEmpty()){
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
        }else {
            servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
        }
        initializers.forEach(webApplicationInitializer ->
                webApplicationInitializer.onStartup(servletContext));
    }



    private WebApplicationInitializer getInitializerInstance(Class<?> aClass) {
        try {
            return (WebApplicationInitializer) aClass.getConstructor().newInstance();
        }catch (Exception e){
            throw new RuntimeException("Failed to instantiate WebApplicationInitializer class",e);
        }

    }


    private boolean checkImplements(Class<?> aClass) {
        return !aClass.isInterface()
                && !Modifier.isAbstract(aClass.getModifiers())
                && WebApplicationInitializer.class.isAssignableFrom(aClass);
    }
}
