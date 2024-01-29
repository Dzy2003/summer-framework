package com.duan.testContext.testPropertyResolver;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.context.BeanDefinitionScanner;

import java.util.Scanner;

/**
 * @author 白日
 * @create 2024/1/24 0:43
 * @description
 */

public class test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        BeanDefinitionScanner scanner = new BeanDefinitionScanner(context);
        scanner.scan("com.duan.testContext.testPropertyResolver");
        System.out.println(context.beans);
    }
}
