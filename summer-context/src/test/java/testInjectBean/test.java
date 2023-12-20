package testInjectBean;

import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author 白日
 * @create 2023/12/19 21:04
 * @description
 */

public class test {
    @Test
    public void testInjectBean(){
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(config.class)) {
            bean2 bean2 = context.getBean(bean2.class);
            bean2.useBean1();
            Controller controller = context.getBean(Controller.class);
            controller.userService();
        }

    }
}
