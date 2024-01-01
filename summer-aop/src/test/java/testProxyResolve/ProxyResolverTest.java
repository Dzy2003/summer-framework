package testProxyResolve;

import com.duan.summer.aop.ProxyResolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyResolverTest {

    @Test
    public void testProxyResovler() {
        OriginBean origin = new OriginBean();
        origin.name = "Bob";


        // create proxy:
        OriginBean proxy = new ProxyResolver().createProxy(origin, new PoliteInvocationHandler());

        // Proxy类名,类似OriginBean$ByteBuddy$9hQwRy3T:
        System.out.println(proxy.getClass().getName());

        // proxy class, not origin class:
        assertNotSame(OriginBean.class, proxy.getClass());
        // proxy.name is null:
        assertNull(proxy.name);
        proxy.hello();
    }
}
