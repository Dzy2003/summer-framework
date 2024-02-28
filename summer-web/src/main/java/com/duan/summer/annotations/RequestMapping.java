package com.duan.summer.annotations;

import com.duan.summer.handler.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @Author: Xhy
 * @gitee: https://gitee.com/XhyQAQ
 * @copyright: B站: https://space.bilibili.com/152686439
 * @CreateTime: 2023-10-14 20:40
 */


@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {


    // 具体路径
    String value() default "";

    // 具体请求
    RequestType requestMethod() default RequestType.NONE;
}
