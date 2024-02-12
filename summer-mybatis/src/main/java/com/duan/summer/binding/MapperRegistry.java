package com.duan.summer.binding;

import cn.hutool.core.lang.ClassScanner;
import com.duan.summer.builder.annotation.MapperAnnotationBuilder;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 白日
 * @create 2024/1/20 21:48
 * @description
 */

public class MapperRegistry {
    Map<Class<?>, MapperProxyFactory<?>> mappers = new HashMap<>();
    Configuration configuration;
    public MapperRegistry(Configuration configuration){
        this.configuration = configuration;
    }

    public <T> T getMapper(Class<T> type, SqlSession session) {
        MapperProxyFactory<T> mapperProxyFactory =(MapperProxyFactory<T>) mappers.get(type);
        if(mapperProxyFactory == null){
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.createInterfaceProxy(session);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }
    public <T> void addMapper(Class<T> type){
        if(type.isInterface()){
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            mappers.put(type, new MapperProxyFactory<>(type));
            MapperAnnotationBuilder mapperAnnotationBuilder = new MapperAnnotationBuilder(configuration, type);
            mapperAnnotationBuilder.parse();
        }
    }

    public  <T> boolean hasMapper(Class<T> type) {
        return mappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }
}
