package com.duan.testContext.testInjectBean;

import com.duan.summer.annotations.Component;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanFactoryPostProcessor;
import com.duan.summer.exception.BeansException;

/**
 * @author 白日
 * @create 2024/1/18 21:45
 * @description
 */
@Component
public class BeanFactoryPostProcessorImpl implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanDefinitionRegistry definitions) throws BeansException {
        BeanDefinition beanDefinition = definitions.findBeanDefinition(iService.class);
        beanDefinition.setBeanClass(ServiceImpl1.class);
        try {
            beanDefinition.setConstructor(ServiceImpl1.class.getConstructor());
        }catch (Exception e){
            throw new RuntimeException();
        }

    }
}
