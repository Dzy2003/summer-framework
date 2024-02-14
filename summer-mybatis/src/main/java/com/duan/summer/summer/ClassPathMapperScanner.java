package com.duan.summer.summer;

import com.duan.summer.binding.MapperProxyFactory;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.context.BeanDefinitionFactory;
import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanDefinitionScanner;
import com.duan.summer.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 白日
 * @create 2024/2/12 23:19
 * @description Mapper扫描器
 */

public class ClassPathMapperScanner extends BeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathMapperScanner.class);

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected List<BeanDefinition> doScan(String basePackage) {
        List<BeanDefinition> mapperBeanDefinitions = super.doScan(basePackage);
        if (mapperBeanDefinitions.isEmpty()) {
            LOGGER.info("No MyBatis mapper was found in '" +basePackage + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(mapperBeanDefinitions);
        }

        return mapperBeanDefinitions;
    }

    private void processBeanDefinitions(List<BeanDefinition> mapperBeanDefinitions) {
        for (BeanDefinition definition : mapperBeanDefinitions) {
            String name = definition.getName();
            Class<?> mapperInterface = definition.getBeanClass();
            definition.setBeanClass(MapperFactoryBean.class);
            String factoryBeanName = mapperInterface.getSimpleName() + "FactoryBean";
            definition.setName(factoryBeanName);
            try {
                definition.setInitMethod(MapperFactoryBean.class.getMethod("afterPropertiesSet"));
                definition.setInstance(MapperFactoryBean.class.getConstructor(Class.class).newInstance(mapperInterface));
                System.out.println(definition);
                BeanDefinition beanDefinition = BeanDefinitionFactory.createBeanDefinition(
                        MapperFactoryBean.class.getMethod("getMapperProxy"), name);
                beanDefinition.setName(mapperInterface.getSimpleName());
                beanDefinition.setBeanClass(mapperInterface);
                registry.registerBeanDefinition(mapperInterface.getSimpleName(),beanDefinition);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
