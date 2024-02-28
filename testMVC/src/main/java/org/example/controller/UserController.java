package org.example.controller;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import com.duan.summer.annotations.Controller;
import com.duan.summer.annotations.RequestMapping;
import com.duan.summer.handler.RequestType;
import org.example.service.UserService;

/**
 * @author 白日
 * @create 2024/2/24 0:31
 * @description
 */
@Controller
@RequestMapping(value = "users")
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping(value = "id",requestMethod = RequestType.GET)
    public void introduction() {
        userService.introduction();
        System.out.println("我是Controller控制器");
    }
}
