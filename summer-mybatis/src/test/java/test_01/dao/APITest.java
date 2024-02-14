package test_01.dao;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.summer.SqlSessionFactoryBean;
import org.junit.jupiter.api.Test;
import test_04.duan.mapper.EmployeeMapper;

import javax.sql.DataSource;

/**
 * @author 白日
 * @create 2024/1/18 14:04
 * @description
 */
@Component
public class APITest {

    @Test
    public void testMapperProxyFactory(){
//        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
//        Map<String, String> sqlSession = new HashMap<>();
//        sqlSession.put("dao.IUserDao.queryUserAge", "执行queryUserAge代理方法");
//        sqlSession.put("dao.IUserDao.queryUserName", "执行queryUserName代理方法");
//        IUserDao iUserDao = mapperProxyFactory.createInterfaceProxy();
//        System.out.println(iUserDao.queryUserName("11111"));
//        System.out.println(IUserDao.class.getName());
    }
    @Test
    public void testFactoryBean() throws NoSuchMethodException {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class)) {
            EmployeeService service = context.getBean(EmployeeService.class);
            service.selectById(10);
        }
    }
}
