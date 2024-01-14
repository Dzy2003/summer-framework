package com.duan.summer.context_rebuild;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Value;
import com.duan.summer.exception.*;
import com.duan.summer.io.PropertyResolver;
import com.duan.summer.utils.ClassUtils;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author 白日
 * @create 2023/12/12 19:23
 * @description 适配器模式，ApplicationContextImpl继承实现BeanDefinitionRegistry
 */

public class GenericApplicationContext extends ApplicationContextImpl implements BeanDefinitionRegistry{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (beans.put(beanDefinition.getName(), beanDefinition) != null) {
            throw new BeanDefinitionException("Duplicate bean name: " + beanDefinition.getName());
        }
    }

    @Override
    public BeanDefinition findBeanDefinition(String beanName) {
        return beans.get(beanName);
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        BeanDefinition def = findBeanDefinition(requiredType);
        if (def == null) {
            throw new NoSuchBeanDefinitionException(String.format("No bean defined with type '%s'.", requiredType));
        }
        return (T) def.getRequiredInstance();
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getBeans(Class<T> requiredType) {
        List<BeanDefinition> defs = findBeanDefinitions(requiredType);
        if (defs.isEmpty()) {
            return List.of();
        }
        List<T> list = new ArrayList<>(defs.size());
        for (var def : defs) {
            list.add((T) def.getRequiredInstance());
        }
        return list;
    }

    @Override
    public BeanDefinition findBeanDefinition(Class<?> type) {
        List<BeanDefinition> defs = findBeanDefinitions(type);
        if (defs.isEmpty()) {
            return null;
        }
        if (defs.size() == 1) {
            return defs.get(0);
        }
        // more than 1 beans, require @Primary:
        List<BeanDefinition> primaryDefs = defs.stream().filter(BeanDefinition::isPrimary).toList();
        if (primaryDefs.size() == 1) {
            return primaryDefs.get(0);
        }
        if (primaryDefs.isEmpty()) {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, but no @Primary specified.", type.getName()));
        } else {
            throw new NoUniqueBeanDefinitionException(String.format("Multiple bean with type '%s' found, and multiple @Primary specified.", type.getName()));
        }
    }

    private List<BeanDefinition> findBeanDefinitions(Class<?> type) {
        return this.beans.values().stream()
                // 过滤不在type继承体系中的中BeanDefinition
                .filter(def -> type.isAssignableFrom(def.getBeanClass()))
                // 排序:
                .sorted().toList();
    }

    @Nullable
    public BeanDefinition findBeanDefinition(String name, Class<?> requiredType) {
        BeanDefinition def = findBeanDefinition(name);
        if (def == null) {
            return null;
        }
        if (!requiredType.isAssignableFrom(def.getBeanClass())) {
            throw new BeanNotOfRequiredTypeException(String.format("Autowire required type '%s' but bean '%s' has actual type '%s'.", requiredType.getName(),
                    name, def.getBeanClass().getName()));
        }
        return def;
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beans.remove(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beans.containsKey(beanName);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return beans.keySet();
    }

    @Override
    public void registryPropertyResolver(Properties properties) {
        propertyResolver.registryPropertyResolver(properties);
    }

    /**
     * 向BeanDefinition中插入实例化后的对象
     * @param definition Bean定义信息
     */
    @Override
    public Object createBeanAsEarlySingleton(BeanDefinition definition) {
        logger.atDebug().log("Try create bean '{}' as early singleton: {}",
                definition.getName(), definition.getBeanClass().getName());
        //循环依赖报错
        if(!creatingBeanNames.add(definition.getName())){
            throw new UnsatisfiedDependencyException(
                    String.format("Circular dependency detected when create bean '%s'", definition.getName()));
        }
        Executable createFun = definition.getFactoryName() == null ?
                definition.getConstructor() : definition.getFactoryMethod();
        assert createFun != null;
        Parameter[] parameters = createFun.getParameters();//拿到参数
        Annotation[][] annotations = createFun.getParameterAnnotations();//拿到参数的注解
        Object[] args = new Object[parameters.length];//为参数注入值
        for(int i = 0; i < args.length; i++){
            Parameter curParameter = parameters[i];
            Annotation[] curParameterAnnotations = annotations[i];
            Value value = ClassUtils.getAnnotation(curParameterAnnotations, Value.class);
            Autowired autowired = ClassUtils.getAnnotation(curParameterAnnotations, Autowired.class);
            // @Configuration类型的Bean是工厂，不允许使用@Autowired创建:
            final boolean isConfiguration = isConfigurationDefinition(definition);
            if (isConfiguration && autowired != null) {
                throw new BeanCreationException(
                        String.format("Cannot specify @Autowired when create @Configuration bean '%s': %s."
                                , definition.getName(), definition.getBeanClass().getName()));
            }

            // 参数需要@Value或@Autowired两者之一:
            if (value != null && autowired != null) {
                throw new BeanCreationException(
                        String.format("Cannot specify both @Autowired and @Value when create bean '%s': %s."
                                , definition.getName(), definition.getBeanClass().getName()));
            }
            if (value == null && autowired == null) {
                throw new BeanCreationException(
                        String.format("Must specify @Autowired or @Value when create bean '%s': %s.",
                                definition.getName(), definition.getBeanClass().getName()));
            }
            final Class<?> type = curParameter.getType();
            if(value != null){
                args[i] = propertyResolver.getProperty(value.value(),type);
            }else{
                String dependencyName = autowired.name();
                boolean required = autowired.value();
                BeanDefinition dependencyDefinition = dependencyName.isEmpty() ?
                        findBeanDefinition(type) : findBeanDefinition(dependencyName, type);
                if(required && dependencyDefinition == null){
                    throw new BeanCreationException(String.format("Missing autowired bean with type '%s' when create bean '%s': %s.", type.getName(),
                            definition.getName(), definition.getBeanClass().getName()));
                }
                if(dependencyDefinition != null){
                    Object dependencyInstance = dependencyDefinition.getInstance();
                    // 若依赖的Bean未被创建则递归创建
                    if(dependencyInstance == null){
                        dependencyInstance = createBeanAsEarlySingleton(dependencyDefinition);
                    }
                    args[i] = dependencyInstance;
                }else{
                    args[i] =null;
                }
            }
        }
        Object instance = null;
        try {
            if(definition.getFactoryName() == null){
                instance = definition.getConstructor().newInstance(args);
            }else{
                Object bean = getBean(definition.getFactoryName());
                instance = definition.getFactoryMethod().invoke(bean,args);
            }
        }catch (Exception e){
            throw new BeanCreationException(String.format("Exception when create bean '%s': %s",
                    definition.getName(), definition.getBeanClass().getName()), e);
        }
        definition.setInstance(instance);
        instance = callPostProcessor(definition);
        return instance;
    }

    /**
     * 在创建Bean实例之前，调用BeanPostProcessor的postProcessBeforeInitialization方法对Bean实例进行处理
     * @param def BeanDefinition
     * @return 处理后的Bean实例
     */
    private Object callPostProcessor(BeanDefinition def) {
        Object instance = def.getInstance();
        if(instance instanceof ApplicationContextAware){
            ((ApplicationContextAware) instance).setApplicationContext(beans);
        }
        for (BeanPostProcessor processor : beanPostProcessors) {
            Object processed = processor
                    .postProcessBeforeInitialization(def.getInstance(), def.getName());
            if (processed == null) {
                throw new BeanCreationException(String.format("PostBeanProcessor returns null when process bean '%s' by %s", def.getName(), processor));
            }
            //处理后的Bean实例与原本的实例不同，则替换原来的Bean实例
            if(processed != def.getInstance()){
                logger.atDebug().log("Bean '{}' was replaced by post processor {}.",
                        def.getName(), processor.getClass().getName());
                instance = processed;
                def.setInstance(processed);
            }
        }
        return instance;
    }
}
