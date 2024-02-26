package org.example.controller;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import org.example.service.UserService;

/**
 * @author 白日
 * @create 2024/2/24 0:31
 * @description
 */
@Component
public class UserController {
    @Autowired
    UserService userService;
    public void introduction() {
        userService.introduction();
        System.out.println("我是Controller控制器");
    }
}
