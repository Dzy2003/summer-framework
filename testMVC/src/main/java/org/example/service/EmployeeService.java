package org.example.service;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import org.example.aspect.LogAnno;
import org.example.mapper.EmployeeMapper;
import org.example.pojo.Employee;


/**
 * @author 白日
 * @create 2024/2/14 13:50
 * @description
 */
@Component
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    @LogAnno
    public Employee selectById(Long id){
        return employeeMapper.selectByID(id);
    }
}
