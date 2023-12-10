package com.duan.summer.utils;

import com.duan.summer.exception.BeanDefinitionException;

import java.lang.annotation.Annotation;

/**
 * @author 白日
 * @create 2023/12/10 21:27
 * @description
 */

public class  ClassUtils {
    /**
     * 递归查找注解，因为注解是可以继承的，所以需要递归查找
     * @param target 查找注解的类
     * @param annoClass 需要查找的注解
     * @return 找到的注解
     * @param <A> 注解
     */
    public static <A extends Annotation> A findAnnotation(Class<?> target, Class<A> annoClass) {
        A a = target.getAnnotation(annoClass);
        for (Annotation anno : target.getAnnotations()) {
            Class<? extends Annotation> annoType = anno.annotationType();
            if (!annoType.getPackageName().equals("java.lang.annotation")) {
                A found = findAnnotation(annoType, annoClass);
                if (found != null) {
                    if (a != null) {
                        throw new BeanDefinitionException("Duplicate @" + annoClass.getSimpleName() + " found on class " + target.getSimpleName());
                    }
                    a = found;
                }
            }
        }
        return a;
    }
}

