package org.example.mapper;


import com.duan.summer.annotations.*;
import org.example.pojo.Employee;

/**
 * @author 白日
 * @create 2023/11/9 17:30
 */
@Component
public interface EmployeeMapper {
    @Insert("INSERT INTO  employee (name, age, position) VALUES (#{name}, #{age}, #{position})")
    int insert(Employee employee);
    @Select("select * from employee where id = #{id}")
    Employee selectByID(@Param("id") Long id);

    @Delete("delete from employee where id = #{id}")
    int deleteByID(@Param("id") Integer id);

    @Update("update employee set name = #{name}, age = #{age}, position = #{position} where id = #{id}")
    int updateByID(Employee employee);
}
