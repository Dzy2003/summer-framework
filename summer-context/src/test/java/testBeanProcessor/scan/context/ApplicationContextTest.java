package testBeanProcessor.scan.context;

import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;
import com.duan.summer.context_rebuild.ApplicationContext;
import testBeanProcessor.scan.ScanApplication;
import testBeanProcessor.scan.proxy.InjectProxyOnConstructorBean;
import testBeanProcessor.scan.proxy.InjectProxyOnPropertyBean;
import testBeanProcessor.scan.proxy.OriginBean;
import testBeanProcessor.scan.proxy.SecondProxyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author 白日
 * @create 2023/12/23 12:44
 * @description
 */

public class ApplicationContextTest {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ScanApplication.class)){
            // test proxy:
            OriginBean proxy = ctx.getBean(OriginBean.class);
            System.out.println(proxy);
            assertSame(SecondProxyBean.class, proxy.getClass());
            assertEquals("Scan App", proxy.getName());
            assertEquals("v1.0", proxy.getVersion());
            // make sure proxy.field is not injected:
            assertNull(proxy.name);
            assertNull(proxy.version);

            // other beans are injected proxy instance:
            var inject1 = ctx.getBean(InjectProxyOnPropertyBean.class);
            var inject2 = ctx.getBean(InjectProxyOnConstructorBean.class);
            assertSame(proxy, inject1.injected);
            assertSame(proxy, inject2.injected);
        }
    }
}
