package com.duan.summer.context;

import com.duan.summer.annotation.*;
import com.duan.summer.exception.BeansException;
import com.duan.summer.io.ResourceResolver;
import com.duan.summer.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * @author 白日
 * @create 2023/12/12 16:39
 * @description 读取扫描需要加入容器的类，将类信息注册到 BeanDefinitionRegistry
 */

public class AnnotatedBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void registry(Class<?>... clazz){
        for (Class<?> aClass : clazz) {
            registryBeanDefinitions(scanForClassNames(aClass));
        }
    }


    /**
     * 通过反射将带有@ComponetBean注解等需要加入管理的类创建Definitions记录信息
     * @param beanClassNames 扫描的全限名
     */
    private void registryBeanDefinitions(Set<String> beanClassNames) {
        beanClassNames.forEach(className -> {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException(e);
            }
            //是否带有@Component注解
            Component component = ClassUtils.findAnnotation(clazz, Component.class);
            if(component != null) {
                BeanDefinition def = BeanDefinitionFactory.createBeanDefinition(clazz);
                this.registry.registerBeanDefinition(def.getName(), def);
            }
            //是否带有@Configuration注解，将@Bean标注的方法当作工厂方法
            Configuration configuration = ClassUtils.findAnnotation(clazz, Configuration.class);
            if(configuration != null){
                BeanDefinition def = ClassUtils.findFactoryMethods(clazz);
                if(def == null) return;
                this.registry.registerBeanDefinition(def.getName(), def);
            }
        });
    }

    private Set<String> scanForClassNames(Class<?> configClass) {
        ComponentScan ComponentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
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
                    return resource.name().substring(0, resource.name().length() - 6).replace("\\",".");
                }
                return null;
            });
            beanClassNames.addAll(scan);
        }
        Import anImport = configClass.getAnnotation(Import.class);
        if(anImport == null) return beanClassNames;
        for (Class<?> aClass : anImport.value()) {
            beanClassNames.add(aClass.getName());
        }
        logger.debug("扫描出的类名：{}", beanClassNames);
        return beanClassNames;
    }
}
