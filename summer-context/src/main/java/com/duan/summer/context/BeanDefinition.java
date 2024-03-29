package com.duan.summer.context;

import com.duan.summer.exception.BeanCreationException;
import jakarta.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 扫描指定包下所有Bean的元信息，用于创建实例
 */
public class BeanDefinition implements Comparable<BeanDefinition> {

    // 全局唯一的Bean Name:
    private String name;
    // Bean的声明类型:
    private Class<?> beanClass;
    // Bean的实例:
    private Object instance = null;
    // 构造方法/null:
    private Constructor<?> constructor;
    // 工厂方法名称/null:
    private String factoryName;
    // 工厂方法/null:
    private Method factoryMethod;
    // Bean的顺序:
    private int order;
    // 是否标识@Primary:
    private boolean primary;

    private String initMethodName;
    private String destroyMethodName;

    private Method initMethod;
    private Method destroyMethod;
    public BeanDefinition(){}

    public BeanDefinition(String name, Class<?> beanClass, Constructor<?> constructor, int order, boolean primary, String initMethodName,
                          String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.constructor = constructor;
        this.factoryName = null;
        this.factoryMethod = null;
        this.order = order;
        this.primary = primary;
        //constructor.setAccessible(true);
        setInitAndDestroyMethod(initMethodName, destroyMethodName, initMethod, destroyMethod);
    }

    public BeanDefinition(String name, Class<?> beanClass, String factoryName, Method factoryMethod, int order, boolean primary, String initMethodName,
                          String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.constructor = null;
        this.factoryName = factoryName;
        this.factoryMethod = factoryMethod;
        this.order = order;
        this.primary = primary;
        factoryMethod.setAccessible(true);
        setInitAndDestroyMethod(initMethodName, destroyMethodName, initMethod, destroyMethod);
    }


    private void setInitAndDestroyMethod(String initMethodName, String destroyMethodName, Method initMethod, Method destroyMethod) {
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        if (initMethod != null) {
            initMethod.setAccessible(true);
        }
        if (destroyMethod != null) {
            destroyMethod.setAccessible(true);
        }
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setInitMethod(Method initMethod) {
        this.initMethod = initMethod;
    }

    public void setDestroyMethod(Method destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    @Nullable
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    @Nullable
    public String getFactoryName() {
        return this.factoryName;
    }

    @Nullable
    public Method getFactoryMethod() {
        return this.factoryMethod;
    }

    @Nullable
    public Method getInitMethod() {
        return this.initMethod;
    }

    @Nullable
    public Method getDestroyMethod() {
        return this.destroyMethod;
    }

    @Nullable
    public String getInitMethodName() {
        return this.initMethodName;
    }

    @Nullable
    public String getDestroyMethodName() {
        return this.destroyMethodName;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Nullable
    public Object getInstance() {
        return this.instance;
    }

    public Object getRequiredInstance() {
        if (this.instance == null) {
            throw new BeanCreationException(String.format("Instance of bean with name '%s' and type '%s' is not instantiated during current stage.",
                    this.getName(), this.getBeanClass().getName()));
        }
        return this.instance;
    }

    public void setInstance(Object instance) {
        Objects.requireNonNull(instance, "Bean instance is null.");

        this.instance = instance;
    }


    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public String toString() {
        return "BeanDefinition [name=" + name + ", beanClass=" + beanClass.getName() + ", factory=" + getCreateDetail() + ", init-method="
                + (initMethod == null ? "null" : initMethod.getName()) + ", destroy-method=" + (destroyMethod == null ? "null" : destroyMethod.getName())
                + ",init-name = " + initMethodName + ", primary=" + primary + ", instance=" + instance + "]" + "\n";
    }

    String getCreateDetail() {
        if (this.factoryMethod != null) {
            String params = String.join(", ", Arrays.stream(this.factoryMethod.getParameterTypes()).map(t -> t.getSimpleName()).toArray(String[]::new));
            return this.factoryMethod.getDeclaringClass().getSimpleName() + "." + this.factoryMethod.getName() + "(" + params + ")";
        }
        return null;
    }

    @Override
    public int compareTo(BeanDefinition def) {
        int cmp = Integer.compare(this.order, def.order);
        if (cmp != 0) {
            return cmp;
        }
        return this.name.compareTo(def.name);
    }
}
