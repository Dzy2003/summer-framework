package testAop;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2024/1/6 16:03
 * @description
 */

public class AopTest {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppScan.class)) {
            Controller controller = context.getBean(Controller.class);
            controller.testInject();
            controller.testInject();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test(){
        System.out.println(TestServiceImpl.class.isAnnotationPresent(target.class));
    }
}
