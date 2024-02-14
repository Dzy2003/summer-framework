package test_01.dao;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import test_04.duan.mapper.EmployeeMapper;

/**
 * @author 白日
 * @create 2024/2/14 13:50
 * @description
 */
@Component
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    public void selectById(int id){
        employeeMapper.selectByID(id);
    }
}
