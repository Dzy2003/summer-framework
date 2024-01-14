package testProxyResolve;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PoliteInvocationHandler implements InvocationHandler {
    int index = 0;
    Object target;
    public PoliteInvocationHandler(Object target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originMethod = target.getClass()
                .getDeclaredMethod(method.getName(), method.getParameterTypes());
        // 修改标记了@Polite的方法返回值:
        if (originMethod.getAnnotation(Polite.class) == null) {
            return method.invoke(proxy, args);
        }
        if(index == 3) return method.invoke(target);
        index++;
        System.out.println("执行被代理方法前" + index);
        Object res = invoke(proxy, method, args);
        System.out.println("执行被代理方法后" + index);
        index--;
        return res;
    }
}
