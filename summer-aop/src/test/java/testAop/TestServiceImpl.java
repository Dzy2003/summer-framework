package testAop;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Order;

/**
 * @author 白日
 * @create 2024/1/6 16:00
 * @description
 */
@Component
public class TestServiceImpl implements TestService{
    @Override
    @target
    public String print(String msg) {
        System.out.println("我执行了，参数是："+msg);
        return msg;
    }

    @Override
    public void sendUser() {
        System.out.println("----发送信息---");
    }
}
