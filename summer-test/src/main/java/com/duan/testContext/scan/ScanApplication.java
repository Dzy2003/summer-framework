package com.duan.testContext.scan;


import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.annotation.PropertySource;

@ComponentScan
@PropertySource("jdbc.properties")
public class ScanApplication {

}
