package com.duan.summer.context;

import com.duan.summer.annotation.*;
import com.duan.summer.exception.BeanDefinitionException;
import com.duan.summer.exception.BeansException;
import com.duan.summer.io.PropertyResolver;
import com.duan.summer.io.Resource;
import com.duan.summer.io.ResourceResolver;
import com.duan.summer.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author 白日
 * @create 2023/12/10 20:15
 * @description
 */

public class AnnotationConfigApplicationContext {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final PropertyResolver propertyResolver;
    public final Map<String, BeanDefinition> beans;
    public AnnotationConfigApplicationContext(Class<?> configClass, PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;

        // 扫描获取所有Bean的Class类型:
        final Set<String> beanClassNames = scanForClassNames(configClass);

        // 创建Bean的定义:
        this.beans = createBeanDefinitions(beanClassNames);
    }

    /**
     * 通过反射将带有@ComponetBean注解等需要加入管理的类创建Definitions记录信息
     * @param beanClassNames 扫描的全限名
     * @return bean的名称和对应的信息
     */
    private Map<String, BeanDefinition> createBeanDefinitions(Set<String> beanClassNames) {
        Map<String, BeanDefinition> defs = new HashMap<>();
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
                defs.put(def.getName(), def);
            }
            //是否带有@Configuration注解，将@Bean标注的方法当作工厂方法
            Configuration configuration = ClassUtils.findAnnotation(clazz, Configuration.class);
            if(configuration != null){
                BeanDefinition def = findFactoryMethods(clazz);
                defs.put(def.getName(),def);
            }
        });
        return defs;
    }

    private BeanDefinition findFactoryMethods(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            Bean bean = method.getAnnotation(Bean.class);
            if (bean != null) {
                int mod = method.getModifiers();
                if (Modifier.isAbstract(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be abstract.");
                }
                if (Modifier.isFinal(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be final.");
                }
                if (Modifier.isPrivate(mod)) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not be private.");
                }
                Class<?> beanClass = method.getReturnType();
                if (beanClass.isPrimitive()) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return primitive type.");
                }
                if (beanClass == void.class || beanClass == Void.class) {
                    throw new BeanDefinitionException("@Bean method " + clazz.getName() + "." + method.getName() + " must not return void.");
                }
                String factoryBeanName = ClassUtils.getBeanName(clazz);
                logger.atDebug().log("define bean: {}", BeanDefinitionFactory.createBeanDefinition(method,factoryBeanName));
                return BeanDefinitionFactory.createBeanDefinition(method, factoryBeanName);
            }
        }
        return null;
    }


    /**
     * 扫描用户指定扫描的包和@Import导入的类
     * @param configClass 配置类
     * @return 返回类的全限名
     */
    public Set<String> scanForClassNames(Class<?> configClass) {
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
        logger.debug("扫描出的类名：{}", beanClassNames);
        return beanClassNames;
    }

}
