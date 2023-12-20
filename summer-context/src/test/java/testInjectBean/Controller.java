package testInjectBean;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;

/**
 * @author 白日
 * @create 2023/12/20 14:31
 * @description
 */
@com.duan.summer.annotation.Controller
public class Controller {
    @Autowired(value = true, name = "ServiceImpl1")
    iService service;
    public void userService(){
        service.print();
    }
}
