package testAop;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;

/**
 * @author 白日
 * @create 2024/1/6 16:00
 * @description
 */
@Component
@target
public class TestServiceImpl {
    @Autowired
    Mapper mapper;

    public void print(String msg) {
        mapper.select();
        System.out.println("我执行了，参数是："+msg);
    }


    public void sendUser() {
        System.out.println("----发送信息---");
    }
}
