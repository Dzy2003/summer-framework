package testInjectBean;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;
import jakarta.annotation.PostConstruct;

/**
 * @author 白日
 * @create 2023/12/19 21:02
 * @description
 */
@Component
public class Bean2 {
    Bean1 bean1;
    public void useBean1(){
        bean1.print();
        System.out.println(bean1.name);
        System.out.println(bean1.username);
    }
    @Autowired
    public void setBean1(Bean1 bean1){
        this.bean1 = bean1;
    }
    @PostConstruct
    public void init(){
        System.out.println("bean2初始化完成");
    }

}
