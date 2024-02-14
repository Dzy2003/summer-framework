package test_01.dao;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.summer.SqlSessionFactoryBean;
import org.junit.jupiter.api.Test;
import test_04.duan.mapper.EmployeeMapper;
import test_04.duan.mapper.UserMapper;

import javax.sql.DataSource;


public class APITest {
    @Test
    public void testFactoryBean() throws NoSuchMethodException {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class)) {
            EmployeeService service = context.getBean(EmployeeService.class);
            UserService userService = context.getBean(UserService.class);
            System.out.println(userService.selectById(1L));
            System.out.println(service.selectById(10));
        }
    }
}
