package testInjectBean;

import com.duan.summer.annotation.Component;
import jakarta.annotation.PostConstruct;

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
    @PostConstruct
    public void init(){
        System.out.println("bean1初始化完成");
    }
}
