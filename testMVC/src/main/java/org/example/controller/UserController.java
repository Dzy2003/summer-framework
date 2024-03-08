package org.example.controller;

import com.duan.summer.annotations.*;
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
    public void introduction(@RequestParam(value = "id", defaultValue = "2") Integer id) {
        userService.introduction();
        System.out.println(id);
        System.out.println("我是Controller控制器");
    }

    @RequestMapping(value = "name",requestMethod = RequestType.GET)
    public void getName() {
        System.out.println("段志宇");
    }
    @RequestMapping(value = "{id}",requestMethod = RequestType.GET)
    public void getPath() {
        System.out.println("段志宇");
    }
}
