package testAop;

import com.duan.summer.annotation.Around;
import com.duan.summer.annotation.Aspect;
import com.duan.summer.annotation.Component;
import com.duan.summer.aop.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * @author 白日
 * @create 2024/1/6 15:57
 * @description
 */
@Aspect
@Component
public class MyAspect {

    /**
     * 拦截所有方法上携带  MyAopAnnotation 注解的方法
     * @param joinPoint
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Around(targetAnno = target.class)
    public Object testAspect(ProceedingJoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        //方法放行
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("总共用时："+(endTime - startTime));
        return proceed;
    }


    /**
     * 拦截所有方法上携带  MyAopAnnotation 注解的方法
     * @param joinPoint
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Around(targetAnno = target.class)
    public Object testAspect2(ProceedingJoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        //方法放行
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("总共用时："+(endTime - startTime));
        return proceed;
    }
}
