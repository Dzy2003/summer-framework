package com.duan.summer.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/2/2 21:12
 * @description
 */

public class TypeHandlerRegistry {

    private final Map<Class<?>, TypeHandler<?>> typeHandlerMap = new HashMap<>();

    public TypeHandlerRegistry() {
        //TODO 把各种TypeHandler注册到map中
        register(Long.class, new LongTypeHandler());
        register(Integer.class,new IntegerTypeHandler());
    }

    private <T> void register(Class<T> type, TypeHandler<T> typeHandler){
        typeHandlerMap.put(type, typeHandler);
    }
    public <T> TypeHandler<T> getHandler(Class<T> type){
        return (TypeHandler<T>) typeHandlerMap.get(type);
    }
    public boolean hasTypeHandler(Class<?> javaType) {
        return typeHandlerMap.containsKey(javaType);
    }

}
