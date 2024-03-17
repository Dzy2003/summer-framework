package test_01.dao;

import com.duan.summer.annotations.ComponentScan;
import com.duan.summer.annotations.Import;
import com.duan.summer.annotations.PropertySource;
import com.duan.summer.summer.JDBCConfig;
import com.duan.summer.summer.MapperScannerConfigurer;
import com.duan.summer.summer.SqlSessionFactoryBean;
import org.testng.annotations.Ignore;

/**
 * @author 白日
 * @create 2024/1/19 23:03
 * @description
 */
@ComponentScan
@PropertySource("jdbc.properties")
public class Config {
}
