package testInjectBean;

import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.annotation.Configuration;
import com.duan.summer.annotation.PropertySource;

/**
 * @author 白日
 * @create 2023/12/19 21:04
 * @description
 */
@Configuration
@ComponentScan
@PropertySource("jdbc.properties")
public class config {
}
