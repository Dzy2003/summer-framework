package com.duan.testContext.scan.proxy;


import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;
}
