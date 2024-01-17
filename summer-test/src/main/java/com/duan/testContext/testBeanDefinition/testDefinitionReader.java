package com.duan.testContext.testBeanDefinition;

import com.duan.summer.annotation.Component;
import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2023/12/12 19:38
 * @description
 */
@Component
public class testDefinitionReader {
    @Test
   public void testReader(){
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(config.class)) {
            System.out.println(context.beans);
        }
   }
}
