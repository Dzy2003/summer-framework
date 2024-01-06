package testAop;

import com.duan.summer.aop.AbstractAOPProxyCreator;
import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;
import com.duan.summer.context_rebuild.ApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2024/1/6 16:03
 * @description
 */

public class AopTest {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppScan.class)) {
            TestService testService = context.getBean(TestServiceImpl.class);
            testService.print("dwdw");
            testService.sendUser();
            AbstractAOPProxyCreator bean = context.getBean(AbstractAOPProxyCreator.class);
            System.out.println(bean.proxyRule);
            System.out.println(bean.aspectInstance);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
