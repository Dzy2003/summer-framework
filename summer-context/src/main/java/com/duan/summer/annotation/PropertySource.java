package com.duan.summer.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertySource {
    String name() default "";
    String[] value();
}
