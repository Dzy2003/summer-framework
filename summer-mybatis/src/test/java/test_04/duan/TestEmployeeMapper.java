package test_04.duan;

import com.duan.summer.io.Resources;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test_04.duan.mapper.EmployeeMapper;
import test_04.duan.pojo.Employee;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author 白日
 * @create 2023/11/9 17:40
 */

public class TestEmployeeMapper {
    static EmployeeMapper employeeMapper;
    static SqlSession sqlSession;
    @BeforeAll
    public static void init() throws IOException {
        //1.加载mybatis的核心配置文件，获取SqlSessionFactory
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        sqlSession = sqlSessionFactory.openSession();
        employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
    }
    @Test
    public void testInsert(){
        Employee employee = new Employee();
        employee.setName("张三");
        employee.setAge(20);
        employee.setPosition("员工");
        int res = employeeMapper.insert(employee);
        assert res == 1;
    }

    @Test
    public void testSelect(){
        System.out.println(sqlSession);
        Employee employee = employeeMapper.selectByID(10);
        System.out.println(employee);
    }

    @Test
    public void testUpdate(){
        Employee employee = new Employee();
        employee.setId(10);
        employee.setName("陈阳");
        employee.setAge(21);
        employee.setPosition("员工");
        int res = employeeMapper.updateByID(employee);
        assert res == 1;
    }
    @Test
    public void testDelete(){
        int res = employeeMapper.deleteByID(9);
        assert res == 1;
    }

    @AfterEach
    public void destroy(){
        sqlSession.commit();
        sqlSession.close();
    }
}
