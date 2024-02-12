package com.duan.testContext.testBeanDefinition;

import com.duan.summer.annotations.ComponentScan;
import com.duan.summer.annotations.Import;
import com.duan.summer.annotations.PropertySource;
import com.duan.summer.utils.ClassUtils;

/**
 * @author 白日
 * @create 2023/12/10 22:12
 * @description
 */
@ComponentScan({"testBeanDefinition"})
@Import(ClassUtils.class)
@PropertySource("jdbc.properties")
public class config {
}
