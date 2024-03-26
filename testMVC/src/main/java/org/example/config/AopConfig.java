package org.example.config;

import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.aop.AOPProxyFactory;

/**
 * @author 白日
 * @create 2024/3/26 21:49
 * @description
 */
@Configuration
public class AopConfig {
    @Bean
    AOPProxyFactory createAroundProxyBeanPostProcessor() {
        return new AOPProxyFactory();
    }
}
