package com.duan.summer.summer;

import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanFactoryPostProcessor;
import com.duan.summer.exception.BeansException;

/**
 * @author 白日
 * @create 2024/2/12 23:19
 * @description 配置Mapper扫描器
 */

public class MapperScannerConfigurer implements BeanFactoryPostProcessor {
    String basePackage;

    @Override
    public void postProcessBeanFactory(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.scan(basePackage);
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
