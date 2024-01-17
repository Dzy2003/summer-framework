package com.duan.summer.utils;

import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Component;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.context.BeanDefinitionFactory;
import com.duan.summer.exception.BeanDefinitionException;
import jakarta.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

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

    public static String getBeanName(Class<?> clazz) {
        String name = "";
        // 查找@Component:
        Component component = clazz.getAnnotation(Component.class);
        if (component != null) {
            // @Component exist:
            name = component.value();
        } else {
            // 未找到@Component，继续在其他注解中查找@Component:
            for (Annotation anno : clazz.getAnnotations()) {
                if (findAnnotation(anno.annotationType(), Component.class) != null) {
                    try {
                        name = (String) anno.annotationType().getMethod("value").invoke(anno);
                    } catch (ReflectiveOperationException e) {
                        throw new BeanDefinitionException("Cannot get annotation value.", e);
                    }
                }
            }
        }
        if (name.isEmpty()) {
            // default name: "HelloWorld" => "helloWorld"
            name = clazz.getSimpleName();
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        return name;
    }
    /**
     * Get bean name by:
     *
     * <code>
     * @Bean
     * Hello createHello() {}
     * </code>
     */
    public static String getBeanName(Method method) {
        Bean bean = method.getAnnotation(Bean.class);
        String name = bean.value();
        if (name.isEmpty()) {
            name = method.getName();
        }
        return name;
    }

    /**
     * 查找注解方法,
     * @param clazz 类对象
     * @param annoClass 注解类型
     * @return 返回注解方法，如果找不到则返回null
     */
    @Nullable
    public static Method findAnnotationMethod(Class<?> clazz, Class<? extends Annotation> annoClass) {
        // 首先尝试获取声明在类中的方法：
        List<Method> ms = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(annoClass)).map(m -> {
            if (m.getParameterCount() != 0) {
                throw new BeanDefinitionException(
                        String.format("Method '%s' with @%s must not have argument: %s", m.getName(), annoClass.getSimpleName(), clazz.getName()));
            }
            return m;
        }).toList();
        if (ms.isEmpty()) {
            return null;
        }
        if (ms.size() == 1) {
            return ms.get(0);
        }
        throw new BeanDefinitionException(String.format("Multiple methods with @%s found in class: %s", annoClass.getSimpleName(), clazz.getName()));
    }

    @Nullable
    public static BeanDefinition findFactoryMethods(Class<?> clazz) {
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
                return BeanDefinitionFactory.createBeanDefinition(method, factoryBeanName);
            }
        }
        return null;
    }
    @Nullable
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A getAnnotation(Annotation[] annos, Class<A> annoClass) {
        for (Annotation anno : annos) {
            if (annoClass.isInstance(anno)) {
                return (A) anno;
            }
        }
        return null;
    }
}

