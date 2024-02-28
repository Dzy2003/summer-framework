package com.duan.summer.annotations;



import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value();

    String defaultValue() default "";

    boolean Required() default true;
}
