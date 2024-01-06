package testAop;

import com.duan.summer.annotation.Bean;
import com.duan.summer.annotation.Configuration;
import com.duan.summer.aop.AbstractAOPProxyCreator;

/**
 * @author 白日
 * @create 2024/1/6 16:02
 * @description
 */
@Configuration
public class AppScan {
    @Bean
    AbstractAOPProxyCreator createAroundProxyBeanPostProcessor() {
        return new AbstractAOPProxyCreator();
    }

}
