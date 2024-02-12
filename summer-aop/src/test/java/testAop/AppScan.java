package testAop;

import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.aop.AOPProxyCreator;

/**
 * @author 白日
 * @create 2024/1/6 16:02
 * @description
 */
@Configuration
public class AppScan {
    @Bean
    AOPProxyCreator createAroundProxyBeanPostProcessor() {
        return new AOPProxyCreator();
    }

}
