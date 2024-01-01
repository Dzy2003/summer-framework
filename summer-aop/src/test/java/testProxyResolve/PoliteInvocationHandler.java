package testProxyResolve;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PoliteInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
        // 修改标记了@Polite的方法返回值:
        if (method.getAnnotation(Polite.class) == null) {
            return method.invoke(bean, args);
        }
        System.out.println("执行被代理方法前");
        Object res = method.invoke(bean, args);
        System.out.println("执行被代理方法后");
        return res;
    }
}
