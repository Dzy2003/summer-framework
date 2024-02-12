package com.duan.testContext.testCreateBeanInstance;

import com.duan.summer.annotations.ComponentScan;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.annotations.PropertySource;

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
