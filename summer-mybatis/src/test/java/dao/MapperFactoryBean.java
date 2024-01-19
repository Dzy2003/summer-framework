package dao;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Mapper;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.context.BeanDefinitionFactory;
import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanFactoryPostProcessor;
import com.duan.summer.exception.BeansException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 白日
 * @create 2024/1/19 22:09
 * @description
 */
@Component
public class MapperFactoryBean implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanDefinitionRegistry definitions) throws BeansException {
        Set<String> beanDefinitionNames = definitions.getBeanDefinitionNames();
        Set<String> copyOf = Set.copyOf(beanDefinitionNames);
        for (String definitionName : copyOf) {
            BeanDefinition definition = definitions.findBeanDefinition(definitionName);
            Class<?> intefaceClass = definition.getBeanClass();
            if(definition.getBeanClass().isAnnotationPresent(Mapper.class)){
                definition.setBeanClass(MapperProxyFactory.class);
                try {
                    definition.setInstance(MapperProxyFactory.class.getConstructor(Class.class).newInstance(intefaceClass));
                    BeanDefinition beanDefinition = BeanDefinitionFactory.createBeanDefinition(
                            MapperProxyFactory.class.getMethod("createInterfaceProxy"), definitionName);
                    beanDefinition.setName(intefaceClass.getSimpleName());
                    beanDefinition.setBeanClass(intefaceClass);
                    definitions.registerBeanDefinition(intefaceClass.getSimpleName(),beanDefinition);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
