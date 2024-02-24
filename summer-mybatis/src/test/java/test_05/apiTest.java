package test_05;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author 白日
 * @create 2024/2/13 20:50
 * @description
 */

public class apiTest {
    @Test
    public void testSqlSessionFactoryBean() throws IOException {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        DruidDataSourceFactory druidDataSourceFactory = new DruidDataSourceFactory();
//        Properties properties = new Properties();
//        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));
//        druidDataSourceFactory.setProperties(properties);
//        sqlSessionFactoryBean.setDataSource(druidDataSourceFactory.getDataSource());
//        //sqlSessionFactoryBean.setConfigLocation("mybatis-config-datasource.xml");
//        sqlSessionFactoryBean.setMapperPackage("mapper");
//        sqlSessionFactoryBean.afterPropertiesSet();
//        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getSqlSessionFactory();
//        MapperFactoryBean<IUserDao> iUserDaoMapperFactoryBean = new MapperFactoryBean<>(IUserDao.class);
//        IUserDao mapperProxy = iUserDaoMapperFactoryBean.getMapperProxy();
//        System.out.println(mapperProxy.queryUserInfoById(1L));
    }
}
