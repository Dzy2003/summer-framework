package com.duan.summer.summer;

import com.duan.summer.annotations.Configuration;
import com.duan.summer.annotations.Value;
import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanFactoryPostProcessor;
import com.duan.summer.exception.BeansException;
import org.testng.Assert;

/**
 * @author 白日
 * @create 2024/2/12 23:19
 * @description 配置Mapper扫描器
 */
@Configuration
public class MapperScannerConfigurer implements BeanFactoryPostProcessor {
    @Value("${mybatis.basePackage}")
    String basePackage;

    @Override
    public void postProcessBeanFactory(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        Assert.assertNotNull(basePackage);
        scanner.scan(basePackage);
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
