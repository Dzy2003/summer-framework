package com.duan.testContext.scan.proxy;


import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;
}
