package test_01.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.annotations.Value;

import javax.sql.DataSource;

/**
 * @author 白日
 * @create 2024/2/14 22:49
 * @description
 */
@Configuration
public class JDBCConfig {
    @Bean
    public DataSource dataSource(@Value("${driver}") String driver,
                                 @Value("${url}")  String url,
                                 @Value("${username}") String username,
                                 @Value("${password}") String password){
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
