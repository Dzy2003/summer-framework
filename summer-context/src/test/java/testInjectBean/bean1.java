package testInjectBean;

import com.duan.summer.annotation.Component;

/**
 * @author 白日
 * @create 2023/12/19 21:02
 * @description
 */
@Component
public class bean1 {
    public void print(){
        System.out.println("我是bean1");
    }
}
