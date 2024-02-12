package com.duan.testContext.testInjectBean;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Controller;

/**
 * @author 白日
 * @create 2023/12/20 14:31
 * @description
 */
@Controller
public class IController {
    @Autowired()
    iService service;
    public void userService(){
        service.print();
    }
}
