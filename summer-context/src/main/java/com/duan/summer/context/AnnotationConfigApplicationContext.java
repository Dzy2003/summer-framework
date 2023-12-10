package com.duan.summer.context;

import com.duan.summer.annotation.Import;
import com.duan.summer.io.PropertyResolver;
import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.io.Resource;
import com.duan.summer.io.ResourceResolver;
import com.duan.summer.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 白日
 * @create 2023/12/10 20:15
 * @description
 */

public class AnnotationConfigApplicationContext {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final PropertyResolver propertyResolver;
    protected final Map<String, BeanDefinition> beans;
    public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;

        // 扫描获取所有Bean的Class类型:
        final Set<String> beanClassNames = scanForClassNames(configClass);

        // 创建Bean的定义:
        this.beans = createBeanDefinitions(beanClassNames);
    }

    private Map<String, BeanDefinition> createBeanDefinitions(Set<String> beanClassNames) {
        return null;
    }

    public Set<String> scanForClassNames(Class<?> configClass) {
        ComponentScan ComponentScanAnnotation = ClassUtils.findAnnotation(configClass, ComponentScan.class);
        String[] scanPackages;
        if(ComponentScanAnnotation == null || ComponentScanAnnotation.value().length == 0){
            scanPackages = new String[]{configClass.getPackageName()};
        }else{
            scanPackages = ComponentScanAnnotation.value();
        }
        Set<String> beanClassNames = new HashSet<>();
        for (String scanPackage : scanPackages) {
            List<String> scan = new ResourceResolver(scanPackage).scan(resource -> {
                if (resource.name().endsWith(".class")) {
                    return resource.name().substring(0, resource.name().length() - 6);
                }
                return null;
            });
            beanClassNames.addAll(scan);
        }
        Import anImport = configClass.getAnnotation(Import.class);
        for (Class<?> aClass : anImport.value()) {
            beanClassNames.add(aClass.getName());
        }
        return beanClassNames;
    }

}
