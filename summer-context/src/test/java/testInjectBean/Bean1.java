package testInjectBean;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Value;
import jakarta.annotation.PostConstruct;

/**
 * @author 白日
 * @create 2023/12/19 21:02
 * @description
 */
@Component
public class Bean1 {
    @Value("段志宇")
    String name;
    @Value("${jdbc.username}")
    String username;
    public void print(){
        System.out.println("我是bean1");
    }
    @PostConstruct
    public void init(){
        System.out.println("bean1初始化完成");
    }
}
