package test_01.dao;

import com.duan.summer.annotations.Component;
import com.duan.summer.annotations.Mapper;
import com.duan.summer.context.BeanDefinition;
import com.duan.summer.context.BeanDefinitionFactory;
import com.duan.summer.context.BeanDefinitionRegistry;
import com.duan.summer.context.BeanFactoryPostProcessor;
import com.duan.summer.exception.BeansException;

import java.util.Set;

/**
 * @author 白日
 * @create 2024/1/19 22:09
 * @description 在BeanDefinition加载完成后使用BeanFactoryPostProcessor修改BeanDefinition
 * 1.将@Mapper标注的接口的Definition的BeanClass修改为FactoryBean并设置其Instance
 * 2.注册一个FactoryBean生成的Bean的Definition到BeanDefinitionRegistry中，这个Bean初始化后就是我们需要的Bean
 */
@Component
public class MapperFactoryBean implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanDefinitionRegistry definitions) throws BeansException {
        Set<String> copyOf = Set.copyOf(definitions.getBeanDefinitionNames());
        for (String definitionName : copyOf) {
            BeanDefinition definition = definitions.findBeanDefinition(definitionName);
            Class<?> mapperInterface = definition.getBeanClass();
            if(definition.getBeanClass().isAnnotationPresent(Mapper.class)){
                definition.setName(mapperInterface.getSimpleName() + "FactoryBean");
                definition.setBeanClass(MapperProxyFactory.class);
                try {
                    definition.setInstance(MapperProxyFactory.class.getConstructor(Class.class).newInstance(mapperInterface));
                    BeanDefinition beanDefinition = BeanDefinitionFactory.createBeanDefinition(
                            MapperProxyFactory.class.getMethod("createInterfaceProxy"), definitionName);
                    beanDefinition.setName(mapperInterface.getSimpleName());
                    beanDefinition.setBeanClass(mapperInterface);
                    definitions.registerBeanDefinition(mapperInterface.getSimpleName(),beanDefinition);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
