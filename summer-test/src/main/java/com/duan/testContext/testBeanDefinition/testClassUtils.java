package com.duan.testContext.testBeanDefinition;

import com.duan.summer.annotations.Component;
import com.duan.summer.io.Resource;
import com.duan.summer.utils.ClassUtils;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2023/12/10 21:37
 * @description
 */
@Component
public class testClassUtils {
    @Test
    public void testFindAnnotation(){
        Component annotation = ClassUtils.findAnnotation(Resource.class, Component.class);
        System.out.println(annotation);
    }
}
