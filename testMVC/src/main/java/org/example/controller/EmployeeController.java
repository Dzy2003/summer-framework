package org.example.controller;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Controller;
import com.duan.summer.annotations.RequestMapping;
import com.duan.summer.annotations.RequestParam;
import com.duan.summer.handler.RequestType;
import org.example.pojo.Employee;
import org.example.service.EmployeeService;

/**
 * @author 白日
 * @create 2024/3/15 19:24
 * @description
 */
@Controller
@RequestMapping("employees")
public class EmployeeController {
    @Autowired
    EmployeeService service;
    @RequestMapping(value = "id", requestMethod = RequestType.GET)
    public Employee getEmployeeByID(@RequestParam("id") Long id) {
        return service.selectById(id);
    }
}
