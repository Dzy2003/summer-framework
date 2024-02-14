package test_05;

import com.duan.summer.datasource.DataSourceFactory;
import com.duan.summer.datasource.druid.DruidDataSourceFactory;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.summer.MapperFactoryBean;
import com.duan.summer.summer.SqlSessionFactoryBean;
import org.junit.jupiter.api.Test;
import test_03.dao.IUserDao;
import test_03.po.User1;
import test_04.duan.mapper.EmployeeMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 白日
 * @create 2024/2/13 20:50
 * @description
 */

public class apiTest {
    @Test
    public void testSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        DruidDataSourceFactory druidDataSourceFactory = new DruidDataSourceFactory();
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));
        druidDataSourceFactory.setProperties(properties);
        sqlSessionFactoryBean.setDataSource(druidDataSourceFactory.getDataSource());
        //sqlSessionFactoryBean.setConfigLocation("mybatis-config-datasource.xml");
        sqlSessionFactoryBean.setMapperPackage("mapper");
        sqlSessionFactoryBean.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getSqlSessionFactory();
        MapperFactoryBean<IUserDao> iUserDaoMapperFactoryBean = new MapperFactoryBean<>(IUserDao.class);
        IUserDao mapperProxy = iUserDaoMapperFactoryBean.getMapperProxy();
        System.out.println(mapperProxy.queryUserInfoById(1L));
    }
}
