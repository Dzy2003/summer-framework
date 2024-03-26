package org.example.aspect;

import com.duan.summer.annotations.Around;
import com.duan.summer.annotations.Aspect;
import com.duan.summer.annotations.Component;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.aop.ProceedingJoinPoint;

/**
 * @author 白日
 * @create 2024/3/26 21:40
 * @description
 */
@Component
@Aspect
public class LogAspect {
    @Around(targetAnno = LogAnno.class)
    public Object around(ProceedingJoinPoint joinPoint) {
        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        System.out.println("调用" + joinPoint.getMethod().getName() + "方法，耗时：" + (System.currentTimeMillis() - begin));
        return proceed;
    }
}
