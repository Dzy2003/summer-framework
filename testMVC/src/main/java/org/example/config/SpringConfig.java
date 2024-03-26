package org.example.config;

import com.duan.summer.annotations.ComponentScan;
import com.duan.summer.annotations.PropertySource;

/**
 * @author 白日
 * @create 2024/2/24 0:32
 * @description
 */
@ComponentScan({"org.example.service","org.example.config","org.example.aspect"})
@PropertySource("jdbc.properties")
public class SpringConfig {
}
