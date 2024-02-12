package com.duan.testContext.scan;


import com.duan.summer.annotations.ComponentScan;
import com.duan.summer.annotations.PropertySource;

@ComponentScan
@PropertySource("jdbc.properties")
public class ScanApplication {

}
