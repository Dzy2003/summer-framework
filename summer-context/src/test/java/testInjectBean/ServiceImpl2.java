package testInjectBean;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Primary;

/**
 * @author 白日
 * @create 2023/12/20 14:29
 * @description
 */
@Component()
public class ServiceImpl2 implements iService{
    @Override
    public void print() {
        System.out.println("我是ServiceImpl2");
    }
}
