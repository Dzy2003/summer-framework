package com.duan.testContext.testApplicationContextAware;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.Aware;
import org.junit.jupiter.api.Test;


/**
 * @author 白日
 * @create 2024/1/4 18:14
 * @description
 */

public class test {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ScanApplication.class)) {
            System.out.println(context.getBean(ApplicationContextAwareImpl.class).beans.size());

        }
    }
    @Test
    public void test() {
        ApplicationContextAwareImpl applicationContextAware = new ApplicationContextAwareImpl();
    }

}
