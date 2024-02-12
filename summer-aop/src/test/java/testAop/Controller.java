package testAop;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;

/**
 * @author 白日
 * @create 2024/1/11 22:21
 * @description
 */
@Component
public class Controller {
    @Autowired
    TestServiceImpl service;
    public void testInject() throws InterruptedException {
        System.out.println(service.getClass());
        service.print("dawd");
        service.sendUser();
    }
}
