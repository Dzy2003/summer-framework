import com.duan.summer.binding.MapperProxy;
import com.duan.summer.binding.MapperProxyFactory;
import dao.IUserDao;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/18 14:04
 * @description
 */

public class APITest {
    @Test
    public void testMapperProxyFactory(){
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("dao.IUserDao.queryUserAge", "执行queryUserAge代理方法");
        sqlSession.put("dao.IUserDao.queryUserName", "执行queryUserName代理方法");
        IUserDao iUserDao = mapperProxyFactory.createInterfaceProxy(sqlSession);
        System.out.println(iUserDao.queryUserName("11111"));
        System.out.println(IUserDao.class.getName());
    }
}
