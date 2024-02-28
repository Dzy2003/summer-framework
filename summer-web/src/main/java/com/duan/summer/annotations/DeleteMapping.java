package com.duan.summer.annotations;

import java.lang.annotation.*;

/**
 * @author 白日
 * @create 2024/2/27 14:35
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeleteMapping {
    String value();
}
