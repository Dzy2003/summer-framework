package com.duan.testContext.scan.proxy;


import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}
