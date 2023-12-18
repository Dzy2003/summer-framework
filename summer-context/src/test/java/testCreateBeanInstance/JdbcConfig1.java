package testCreateBeanInstance;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Configuration;

/**
 * @author 白日
 * @create 2023/12/18 20:50
 * @description
 */
@Configuration
public class JdbcConfig1 {
    @Bean
    public DataSource1 createDataSource1(@Autowired DataSource dataSource){
        DataSource1 dataSource1 = new DataSource1();
        dataSource.setUrl(dataSource.url);
        dataSource.setDriverClassName(dataSource.driverClassName);
        dataSource.setUsername(dataSource.username);
        dataSource.setPassword(dataSource.password);
        return dataSource1;
    }
}
