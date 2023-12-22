package testInjectBean;

import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Configuration;
import com.duan.summer.annotation.Value;

/**
 * @author 白日
 * @create 2023/12/21 17:02
 * @description
 */
@Configuration
public class JdbcConfig {
    @Bean(initMethod = "init")
    public DataSource dataSource(@Value("${jdbc.username}") String username,
                                 @Value("${jdbc.password}") String password,
                                 @Value("${jdbc.url}") String url,
                                 @Value("${jdbc.driver}") String driver){
        DataSource dataSource = new DataSource();
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        return dataSource;
    }

    public void init(){
        System.out.println("Datasource的init方法被调用");
    }
}
