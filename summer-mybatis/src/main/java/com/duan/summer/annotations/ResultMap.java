package com.duan.summer.annotations;

import java.lang.annotation.*;

/**
 * @author 白日
 * @create 2024/2/11 15:22
 * @description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ResultMap {
    String value();
}
