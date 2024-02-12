package com.duan.testContext.scan.proxy;


import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}
