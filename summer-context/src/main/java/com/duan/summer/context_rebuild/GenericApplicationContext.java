package com.duan.summer.context_rebuild;

import com.duan.summer.context.BeanDefinition;
import com.duan.summer.exception.BeanDefinitionException;
import com.duan.summer.exception.NoUniqueBeanDefinitionException;

import java.util.List;
import java.util.Set;

/**
 * @author 白日
 * @create 2023/12/12 19:23
 * @description 适配器模式，ApplicationContextImpl继承实现BeanDefinitionRegistry
 */

public class GenericApplicationContext extends ApplicationContextImpl implements BeanDefinitionRegistry{

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
    public BeanDefinition findBeanDefinition(Class<?> type) {
        List<BeanDefinition> defs = this.beans.values().stream()
                // 过滤不在type继承体系中的中BeanDefinition
                .filter(def -> type.isAssignableFrom(def.getBeanClass()))
                // 排序:
                .sorted().toList();
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
}
