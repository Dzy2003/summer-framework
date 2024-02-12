package com.duan.testContext.testCreateBeanInstance;

import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.annotations.Value;

/**
 * @author 白日
 * @create 2023/12/15 17:04
 * @description
 */
@Configuration
public class JdbcConfig {
    @Bean(initMethod = "init")
    public DataSource createDataSource(@Value("${jdbc.username}") String username,
                           @Value("${jdbc.password}") String password,
                           @Value("${jdbc.url}") String url,
                           @Value("${jdbc.driver}") String driver) {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    public void init(){
        System.out.println("DataSource的init方法被调用");
    }
}
