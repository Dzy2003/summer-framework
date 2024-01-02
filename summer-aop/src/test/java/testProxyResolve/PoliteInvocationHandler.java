package testProxyResolve;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PoliteInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object originBean, Method method, Object[] args) throws Throwable {
        Method originMethod = originBean.getClass()
                .getDeclaredMethod(method.getName(), method.getParameterTypes());
        // 修改标记了@Polite的方法返回值:
        if (originMethod.getAnnotation(Polite.class) == null) {
            return method.invoke(originBean, args);
        }
        System.out.println("执行被代理方法前");
        Object res = method.invoke(originBean, args);
        System.out.println("执行被代理方法后");
        return res;
    }
}
