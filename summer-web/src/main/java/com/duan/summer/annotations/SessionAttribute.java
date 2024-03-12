package com.duan.summer.annotations;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionAttribute {
    String value() default "";

    boolean required() default true;
}
