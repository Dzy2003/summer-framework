package testApplicationContextAware;

import com.duan.summer.annotation.Component;
import com.duan.summer.context_rebuild.ApplicationContext;
import com.duan.summer.context_rebuild.ApplicationContextAware;
import com.duan.summer.context_rebuild.BeanDefinition;

import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/4 18:13
 * @description
 */
@Component
public class ApplicationContextAwareImpl implements ApplicationContextAware {
    Map<String, BeanDefinition> beans;
    @Override
    public void setApplicationContext(Map<String, BeanDefinition> beans) {
        this.beans = beans;
    }
}
