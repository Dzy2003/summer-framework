package com.duan.summer.context_rebuild;

import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Order;
import com.duan.summer.annotation.Primary;
import com.duan.summer.exception.BeanDefinitionException;
import com.duan.summer.utils.ClassUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author 白日
 * @create 2023/12/11 18:32
 * @description
 */

public class BeanDefinitionFactory {
    /**
     * 创建BeanDefinition，无工厂方法版本，通常是@Conponent注解标识
     * @param clazz 类
     * @return BeanDefinition
     */
    public static BeanDefinition createBeanDefinition(Class<?> clazz) {
        return new BeanDefinition(
                ClassUtils.getBeanName(clazz),
                clazz,
                getSuitableConstructor(clazz),
                getOrder(clazz),
                clazz.isAnnotationPresent(Primary.class),
                null,null,
                ClassUtils.findAnnotationMethod(clazz, PostConstruct.class),
                ClassUtils.findAnnotationMethod(clazz, PreDestroy.class));

    }

    /**
     * 工厂方法：在spring中注册工厂的bean不会返回工厂的实例，而是返回工厂下带有bean的工厂方法返回的实例
     * 创建BeanDefinition，带有工厂方法版本，通常是@Confignation注解标识，下面带有@Bean的的方法为工厂方法
     * @param method 工厂方法
     * @param factoryBeanName 工厂bean名称
     * @return BeanDefinition bean的定义信息
     */
    public static BeanDefinition createBeanDefinition(Method method, String factoryBeanName) {
        String destroyMethod = method.getAnnotation(Bean.class).destroyMethod();
        String initMethod = method.getAnnotation(Bean.class).initMethod();
        return new BeanDefinition(
                ClassUtils.getBeanName(method),
                method.getReturnType(),
                factoryBeanName,
                method,
                getOrder(method),
                method.isAnnotationPresent(Primary.class),
                initMethod.isEmpty() ? null : initMethod,
                destroyMethod.isEmpty() ? null : destroyMethod,
                null,null
        );
    }

    private static Constructor<?> getSuitableConstructor(Class<?> clazz) {
        Constructor<?>[] cons = clazz.getConstructors();
        if (cons.length == 0) {
            cons = clazz.getDeclaredConstructors();
            if (cons.length != 1) {
                throw new BeanDefinitionException("More than one constructor found in class " + clazz.getName() + ".");
            }
        }
        if (cons.length != 1) {
            throw new BeanDefinitionException("More than one public constructor found in class " + clazz.getName() + ".");
        }
        return cons[0];
    }

    private static int getOrder(Class<?> clazz){
        Order annotation = clazz.getAnnotation(Order.class);
        return annotation == null ? 100 : annotation.value();
    }
    private static int getOrder(Method method){
        Order annotation = method.getAnnotation(Order.class);
        return annotation == null ? 100 : annotation.value();
    }


}
