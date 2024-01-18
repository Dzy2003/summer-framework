package com.duan.testContext.testInjectBean;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Primary;

/**
 * @author 白日
 * @create 2023/12/20 14:28
 * @description
 */

public class ServiceImpl1 implements iService{
    @Override
    public void print() {
        System.out.println("我是ServiceImpl1");
    }
}
