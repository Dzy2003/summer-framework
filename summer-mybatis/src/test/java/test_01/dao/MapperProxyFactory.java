package test_01.dao;

import com.duan.summer.annotation.Bean;

import java.lang.reflect.Proxy;


public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
    @Bean
    public T createInterfaceProxy(){
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface}, (proxy, method, args) -> {
            if(Object.class.equals(method.getDeclaringClass())){
                        return method.invoke(this, args);
                    }else{
                        return "代理成功!";
                    }
                });
    }
}
