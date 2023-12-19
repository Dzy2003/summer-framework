package testInjectBean;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Value;

/**
 * @author 白日
 * @create 2023/12/19 21:02
 * @description
 */
@Component
public class bean2 {

    bean1 bean1;
    @Value("段志宇")
    String name;

    public void useBean1(){
        bean1.print();
        System.out.println(name);
    }
    @Autowired
    public void setBean1(bean1 bean1){
        this.bean1 = bean1;
    }

}
