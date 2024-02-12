package com.duan.summer.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Around {

    /**
     * Invocation handler bean name.
     */
    Class<? extends Annotation> targetAnno();

}
