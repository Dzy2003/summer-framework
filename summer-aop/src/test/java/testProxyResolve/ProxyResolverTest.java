package testProxyResolve;

import com.duan.summer.aop.ProxyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyResolverTest {

    @Test
    public void testProxyFactory() {
        OriginBean origin = new OriginBean();
        origin.name = "Bob";
        // create proxy:
        ProxyInterface1 proxy = (ProxyInterface1)
                new ProxyFactory().createProxy(origin, new PoliteInvocationHandler(origin));

        proxy.hello1();
    }
}
