package com.duan.testContext.testCreateBeanInstance;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2023/12/15 16:58
 * @description
 */

public class test {
    @Test
    public void testCreateInstance(){
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(config.class)) {
            Bean1 bean = (Bean1)context.getBean("bean1");
            bean.print();
            DataSource dataSource = ((DataSource) context.getBean("createDataSource"));
            System.out.println(dataSource);
            ConstructInject constructInject = context.getBean(ConstructInject.class);
            System.out.println(constructInject);
            DataSource source = context.getBean(DataSource.class);
            System.out.println(source);
        }
    }
}
