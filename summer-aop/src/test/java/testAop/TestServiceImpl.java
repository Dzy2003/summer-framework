package testAop;

import com.duan.summer.annotation.Around;
import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Order;

import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/6 16:00
 * @description
 */
@Component
public class TestServiceImpl {
    @Autowired
    Mapper mapper;

    @target
    public String print(String msg) {
        mapper.select();
        System.out.println("我执行了，参数是："+msg);
        return msg;
    }


    public void sendUser() {
        System.out.println("----发送信息---");
    }
}
