package org.example.controller;

import com.duan.summer.annotations.*;
import com.duan.summer.handler.RequestType;
import com.duan.summer.web.ModelAndView;
import org.example.pojo.User;
import org.example.service.UserService;

import java.util.List;

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
    @RequestMapping(value = "id/{name}/{age}",requestMethod = RequestType.GET)
    public User introduction(@RequestParam(value = "id", defaultValue = "2") Integer id,
                             @PathVariable(value = "name") String name,
                             @RequestParam(value = "count") Integer count,
                             @PathVariable(value = "age") Integer age,
                             @RequestHeader(value = "User-Agent") String userAgent,
                             @RequestBody List<User> user) {
        userService.introduction();
        System.out.println(id);
        System.out.println(name);
        System.out.println(count);
        System.out.println(age);
        System.out.println(userAgent);
        System.out.println(user);
        System.out.println("我是Controller控制器");
        return user.get(0);
    }

    @RequestMapping(value = "md",requestMethod = RequestType.GET)
    public ModelAndView getName() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addModel("name", "段志宇");
        modelAndView.addModel("age", 12);
        return modelAndView;
    }
    @RequestMapping(value = "str",requestMethod = RequestType.GET)
    public String getPath() {
        return "redirect:hello";
    }
}
