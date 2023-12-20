package com.duan.summer.context_rebuild;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Configuration;
import com.duan.summer.annotation.Value;
import com.duan.summer.exception.BeanCreationException;
import com.duan.summer.exception.BeanDefinitionException;
import com.duan.summer.exception.BeanNotOfRequiredTypeException;
import com.duan.summer.exception.UnsatisfiedDependencyException;
import com.duan.summer.io.PropertyResolver;
import com.duan.summer.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author 白日
 * @create 2023/12/12 19:22
 * @description
 */

public abstract class ApplicationContextImpl implements ApplicationContext{
    public final Map<String, BeanDefinition> beans;
    Set<String> creatingBeanNames;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public ApplicationContextImpl(){
        beans = new HashMap<>();
        creatingBeanNames = new HashSet<>();
    }
    @Override
    public boolean containsBean(String name) {
        return beans.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        Object instance = beans.get(name).getInstance();
        if(instance == null) throw new BeanNotOfRequiredTypeException("Bean Not Find");
        return instance;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = getBean(name);
        if (!requiredType.isAssignableFrom(bean.getClass())) {
            throw new BeanNotOfRequiredTypeException(String.format("Autowire required type '%s' but bean '%s' has actual type '%s'.", requiredType.getName(),
                    name, bean.getClass().getName()));
        }
        return (T) bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        throw new RuntimeException("Not Support");
    }

    @Override
    public <T> List<T> getBeans(Class<T> requiredType) {
        throw new RuntimeException("Not Support");
    }

    @Override
    public void close() {

    }

    protected void createBean(){
        createConfigurationBean();
        createCommonBean();
    }

    protected void initBean(){
        this.beans.values().forEach(this::injectBean);
        this.beans.values().forEach(this::callInitMethod);
    }


    private void injectBean(BeanDefinition def){
        Class<?> beanClass = def.getBeanClass();
        try {
            for (Field field : beanClass.getDeclaredFields()) {
                tryInjectByField(def,field);
            }
            for (Method method : beanClass.getDeclaredMethods()) {
                tryInjectBySetter(def,method);
            }
        } catch (ReflectiveOperationException e) {
            throw new BeanCreationException(e);
        }
    }

    private void tryInjectByField(BeanDefinition def, Field field) throws ReflectiveOperationException{
        Value value = field.getAnnotation(Value.class);
        Autowired autowired = field.getAnnotation(Autowired.class);
        if (value == null && autowired == null) return;

        if (value != null && autowired != null) {
            throw new BeanCreationException(String.format("Cannot specify both @Autowired and @Value when inject %s.%s for bean '%s': %s",
                    def.getBeanClass().getSimpleName(), field.getName(), def.getName(), def.getBeanClass().getName()));
        }
        checkFieldOrMethod(field);
        field.setAccessible(true);
        if(value != null) {
            PropertyResolver pr = new PropertyResolver();
            logger.atDebug().log("Field injection: {}.{} = {}",
                    def.getBeanClass().getName(), field.getName(), pr.getProperty(value.value(), field.getType()));
            field.set(def.getInstance(), pr.getProperty(value.value(), field.getType()));
        }
        if(autowired != null){
            boolean required = autowired.value();
            Object dependentBean = getDependentBean(autowired, field.getType());
            if(dependentBean == null && required) {
                throw new UnsatisfiedDependencyException(String.format("Dependency bean not found when inject %s.%s for bean '%s': %s"
                        , def.getBeanClass().getSimpleName(),
                        field.getName(), def.getName(), def.getBeanClass().getName()));
            }
            if(dependentBean != null){
                logger.atDebug().log("Field injection: {}.{} = {}",
                        def.getBeanClass().getName(), field.getName(), dependentBean);
                field.set(def.getInstance(), dependentBean);
            }
        }
    }
    private void tryInjectBySetter(BeanDefinition def, Method method) throws ReflectiveOperationException{
        Value value = method.getAnnotation(Value.class);
        Autowired autowired = method.getAnnotation(Autowired.class);
        if (value == null && autowired == null) return;

        if (value != null && autowired != null) {
            throw new BeanCreationException(String.format("Cannot specify both @Autowired and @Value when inject %s.%s for bean '%s': %s",
                    def.getBeanClass().getSimpleName(), method.getName(), def.getName(), def.getBeanClass().getName()));
        }
        checkFieldOrMethod(method);
        if (method.getParameters().length != 1) {
            throw new BeanDefinitionException(
                    String.format("Cannot inject a non-setter method %s for bean '%s': %s",
                            method.getName(), def.getName(), def.getBeanClass().getName()));
        }
        method.setAccessible(true);
        Class<?> injectType = method.getParameterTypes()[0];
        if(value != null) {
            PropertyResolver pr = new PropertyResolver();
            logger.atDebug().log(" injection: {}.{} = {}",
                    def.getBeanClass().getName(), method.getName(), pr.getProperty(value.value(),injectType ));
            method.invoke(def.getInstance(), pr.getProperty(value.value(), injectType));
        }
        if(autowired != null){
            boolean required = autowired.value();
            Object dependentBean = getDependentBean(autowired, injectType);
            if(dependentBean == null && required) {
                throw new UnsatisfiedDependencyException(String.format("Dependency bean not found when inject %s.%s for bean '%s': %s"
                        , def.getBeanClass().getSimpleName(),
                        method.getName(), def.getName(), def.getBeanClass().getName()));
            }
            if(dependentBean != null){
                logger.atDebug().log("Setter injection: {}.{} = {}",
                        def.getBeanClass().getName(), method.getName(), dependentBean);
                method.invoke(def.getInstance(), dependentBean);
            }
        }
    }

    private Object getDependentBean(Autowired autowired,Class<?> injectType){
        return autowired.name().isEmpty() ?
                getBean(injectType) : getBean(autowired.name(),injectType);

    }

    void checkFieldOrMethod(Member m) {
        int mod = m.getModifiers();
        if (Modifier.isStatic(mod)) {
            throw new BeanDefinitionException("Cannot inject static field: " + m);
        }
        if (Modifier.isFinal(mod)) {
            if (m instanceof Field field) {
                throw new BeanDefinitionException("Cannot inject final field: " + field);
            }
            if (m instanceof Method method) {
                logger.warn(
                        "Inject final method should be careful because it is not called on target bean when bean is proxied and may cause NullPointerException.");
            }
        }
    }


    private void callInitMethod(BeanDefinition definition){
        callMethod(definition.getInstance(), definition.getInitMethod(), definition.getInitMethodName());
    }

    private void callMethod(Object instance, Method initMethod, String initMethodName) {
        if(initMethod != null){
            try {
                initMethod.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanCreationException(e);
            }
        }
        if(initMethodName != null && !initMethodName.isEmpty()){
            try {
                Method initmethod = instance.getClass().getDeclaredMethod(initMethodName);
                initmethod.invoke(instance);
            } catch (ReflectiveOperationException e) {
                throw new BeanCreationException(e);
            }
        }
    }

    /**
     * 创建@Configuration注解的工厂
     */
    private void createConfigurationBean(){
        beans.values()
                .stream()
                .filter(this::isConfigurationDefinition)
                .sorted()
                .forEach(this::createBeanAsEarlySingleton);

    }

    /**
     * 创建其它普通的Bean
     */
    private void createCommonBean(){
        List<BeanDefinition> beanDefinitions = beans.values()
                .stream()
                .filter(beanDefinition -> beanDefinition.getInstance() == null)
                .toList();
        beanDefinitions.forEach(beanDefinition -> {
            if(beanDefinition.getInstance() == null) createBeanAsEarlySingleton(beanDefinition);
        });
    }
    protected abstract Object createBeanAsEarlySingleton(BeanDefinition definition);

    protected boolean isConfigurationDefinition(BeanDefinition def) {
        return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;
    }
}
