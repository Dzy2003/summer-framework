package com.duan.testContext.testInjectBean;

import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author 白日
 * @create 2023/12/19 21:04
 * @description
 */

public class test {
    @Test
    public void testInjectBean(){
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(config.class)) {
            Bean2 bean2 = context.getBean(Bean2.class);
            bean2.useBean1();
            IController controller = context.getBean(IController.class);
            controller.userService();
            DataSource bean = context.getBean(DataSource.class);
            System.out.println(bean);
        }
    }
    @Test
    public void testReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        byte[] bytes = inputStream.readAllBytes();
        System.out.println(bytes.length);
    }
}
