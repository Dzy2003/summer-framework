package com.duan.summer.context;

import com.duan.summer.annotations.*;
import com.duan.summer.exception.BeanCreationException;
import com.duan.summer.exception.BeanDefinitionException;
import com.duan.summer.exception.BeanNotOfRequiredTypeException;
import com.duan.summer.exception.UnsatisfiedDependencyException;
import com.duan.summer.io.ConfigResolver;
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

public abstract class ApplicationContextImpl implements ApplicationContext,FileConfigRegistry{
    public final Map<String, BeanDefinition> beans;
    Set<String> creatingBeanNames;
    ConfigResolver configResolver = new ConfigResolver();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    protected final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
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
        BeanDefinition definition = beans.get(name);
        if(definition == null) throw new BeanNotOfRequiredTypeException("Bean Not Find:"+name);
        return definition.getInstance();
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
    public List<Object>  getAllBeans(){
        return beans.values()
                .stream()
                .map(BeanDefinition::getInstance)
                .toList();
    }

    @Override
    public void close() {
        logger.info("Closing {}...", this.getClass().getName());
        this.beans.values().forEach(def -> {
            final Object beanInstance = getProxiedInstance(def);
            callMethod(beanInstance, def.getDestroyMethod(), def.getDestroyMethodName());
        });
        this.beanFactoryPostProcessors.clear();
        this.beanPostProcessors.clear();
        this.creatingBeanNames.clear();
        this.beans.clear();
        logger.info("{} closed.", this.getClass().getName());
    }

    protected void createBean(){
        createConfigurationBean();
        createAspectBean();
        createBeanProcessorBean();
        createCommonBean();
    }

    private void createAspectBean() {
        beans.values()
                .stream()
                .filter(definition -> definition.getBeanClass().isAnnotationPresent(Aspect.class))
                .sorted()
                .forEach(this::createBeanAsEarlySingleton);
    }


    protected void initBean(){
        this.beans.values().forEach(this::callInitMethod);//调用init方法
    }
    protected void initBean(BeanDefinition definition){
        injectBean(definition);
        callInitMethod(definition);
    }


    protected void injectBean(BeanDefinition def){
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
        Object instance = getProxiedInstance(def);
        if(value != null) {
            logger.atDebug().log("Field injection: {}.{} = {}",
                    def.getBeanClass().getName(), field.getName(), configResolver.getConfig(value.value(), field.getType()));
            field.set(instance, configResolver.getConfig(value.value(), field.getType()));
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
                field.set(instance, dependentBean);
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
        Object instance = getProxiedInstance(def);
        Class<?> injectType = method.getParameterTypes()[0];
        if(value != null) {
            logger.atDebug().log(" injection: {}.{} = {}",
                    def.getBeanClass().getName(), method.getName(), configResolver.getConfig(value.value(),injectType ));
            method.invoke(def.getInstance(), configResolver.getConfig(value.value(), injectType));
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
                method.invoke(instance, dependentBean);
            }
        }
    }

    /**
     * 调用BeanPostProcessors的postProcessOnSetProperty方法在对实例注入实例时进行处理
     * @param def bean定义
     * @return 处理后的实例
     */
    private Object getProxiedInstance(BeanDefinition def) {
        Object beanInstance = def.getInstance();
        List<BeanPostProcessor> reversedBeanPostProcessors = new ArrayList<>(this.beanPostProcessors);
        Collections.reverse(reversedBeanPostProcessors);
        for (BeanPostProcessor processor : reversedBeanPostProcessors) {
            Object processedInstance = processor.postProcessOnSetProperty(beanInstance, def.getName());
            if(processedInstance != beanInstance){
                logger.atDebug().log("BeanPostProcessor {} specified injection from {} to {}.",
                        processor.getClass().getSimpleName(),
                        beanInstance.getClass().getSimpleName(), processedInstance.getClass().getSimpleName());
                beanInstance = processedInstance;
            }
        }
        return beanInstance;
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


    protected void callInitMethod(BeanDefinition definition){
        callMethod(definition.getInstance(), definition.getInitMethod(),
                definition.getInitMethodName());
    }

    private void callMethod(Object beanInstance, Method method, String namedMethod) {
        // 调用init/destroy方法:
        if (method != null) {
            try {
                method.invoke(beanInstance);
            } catch (ReflectiveOperationException e) {
                throw new BeanCreationException(e);
            }
        } else if (namedMethod != null) {
            // 查找initMethod/destroyMethod="xyz"，注意是在实际类型中查找:
            Method named = ClassUtils.getNamedMethod(beanInstance.getClass(), namedMethod);
            named.setAccessible(true);
            try {
                named.invoke(beanInstance);
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
     * 创建BeanDefinitionProcessor的Bean
     */
    private void createBeanProcessorBean() {
        beanPostProcessors.addAll(beans.values()
                .stream()
                .filter(this::isBeanPostProcessorDefinition)
               .sorted()
               .map(def -> (BeanPostProcessor) createBeanAsEarlySingleton(def))
                .toList());
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
    public abstract Object createBeanAsEarlySingleton(BeanDefinition definition);

    protected boolean isConfigurationDefinition(BeanDefinition def) {
        return ClassUtils.findAnnotation(def.getBeanClass(), Configuration.class) != null;
    }

    /**
     * 通过BeanDefinition的getBeanClass属性该类判断是否为BeanPostProcessor的实现类
     * @param def BeanDefinition
     * @return true or false
     */
    private boolean isBeanPostProcessorDefinition(BeanDefinition def) {
        return BeanPostProcessor.class.isAssignableFrom(def.getBeanClass());
    }

}
