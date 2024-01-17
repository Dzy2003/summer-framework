package com.duan.testContext.testCreateBeanInstance;

import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.annotation.Configuration;
import com.duan.summer.annotation.PropertySource;

/**
 * @author 白日
 * @create 2023/12/15 17:01
 * @description
 */
@Configuration
@PropertySource("jdbc.properties")
@ComponentScan("testCreateBeanInstance")
public class config {

}
