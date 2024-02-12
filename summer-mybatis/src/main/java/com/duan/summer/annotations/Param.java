package com.duan.summer.annotations;

import java.lang.annotation.*;

/**
 * @author 白日
 * @create 2024/2/4 11:50
 * @description
 */


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String value();
}
