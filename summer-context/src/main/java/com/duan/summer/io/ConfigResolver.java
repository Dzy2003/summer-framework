package com.duan.summer.io;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author 白日
 * @create 2023/12/8 18:36
 * @description
 */

public class ConfigResolver {
    Logger logger = LoggerFactory.getLogger(getClass());
    Map<String, String> configurations = new HashMap<>();
    //类型转换
    Map<Class<?>, Function<String, Object>> converters = new HashMap<>();

    /**
     * 构造方法读取properties文件中的配置信息到properties中
     */
    public ConfigResolver(){
        //this.properties.putAll(System.getenv());
        // register converters:
        registerConverters();
    }

    public void registryConfigResolver(Map<String, Object> map) {
        map.forEach((k,v) ->{
            if(v instanceof String){
                this.configurations.put(k, (String) v);
            }
        });
        logger.debug("PropertyResolver: {} ", configurations);
    }

    /**
     * 通过表达式获取value值
     * @param expression expression 表达式
     * @return value
     */
    @Nullable
    public String getConfig(String expression) {
        PropertyExpr expr = parsePropertyExpr(expression);
        if(expr.defaultValue() != null){
            return getConfig(expr.key(), expr.defaultValue());
        }
        return getRequiredConfig(expr.key());
    }

    /**
     * 重载版本，获取目标类型的value
     * @param expression 表达式
     * @param targetType 目标类型
     * @return value
     */
    @Nullable
    public Object getConfig(String expression, Class<?> targetType) {
        System.out.println(expression);
        String value = expression;
        if(expression.startsWith("${") && expression.endsWith("}")){
            value = getConfig(expression);
        }
        if (value == null) {
            return null;
        }
        return convert(value, targetType);
    }

    /**
     * 类型转换
     * @param value 值
     * @param targetType 目标类型
     * @return 目标类型的值
     */
    private Object convert(String value, Class<?> targetType) {
        if (targetType == null) {
            return value;
        } else {
            Function<String, Object> converter = converters.get(targetType);
            if (converter == null) {
                throw new IllegalArgumentException("No converter found for type [" + targetType.getName() + "]");
            } else {
                try {
                    return converter.apply(value);
                }catch (Exception e) {
                    throw new IllegalArgumentException("Could not convert value of type [" +
                            value.getClass().getName() + "] to required type [" + targetType.getName() + "]: " +
                            e.getMessage());
                }
            }
        }
    }

    /**
     * 重载版本，有默认值的查询
     * @param key 键
     * @param defaultValue 默认值
     * @return value
     */
    String getConfig(String key, String defaultValue) {
        String value = this.configurations.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 解析类似${abc.xyz:defaultValue}的表达式
     * 将传入的表达值解析为PropertyExpr(key,defaultValue)
     * @param expression 表达式
     * @return PropertyExpr
     */
    PropertyExpr parsePropertyExpr(String expression) {
        //判断是否存在默认值
        int indexOf = expression.indexOf(':');
        if(indexOf == -1){
            return new PropertyExpr(expression.substring(2, expression.length() - 1), null);
        }else{
            return new PropertyExpr(expression.substring(2, indexOf),
                    expression.substring(indexOf + 1, expression.length() - 1));
        }
    }

    /**
     * 重载，没有默认值的查询
     * @param key 键
     * @return value
     */
    public String getRequiredConfig(String key) {
        return this.configurations.get(key);
    }


    private void registerConverters() {
        converters.put(String.class, s -> s);
        converters.put(boolean.class, Boolean::parseBoolean);
        converters.put(Boolean.class, Boolean::valueOf);

        converters.put(byte.class, Byte::parseByte);
        converters.put(Byte.class, Byte::valueOf);

        converters.put(short.class, Short::parseShort);
        converters.put(Short.class, Short::valueOf);

        converters.put(int.class, Integer::parseInt);
        converters.put(Integer.class, Integer::valueOf);

        converters.put(long.class, Long::parseLong);
        converters.put(Long.class, Long::valueOf);

        converters.put(float.class, Float::parseFloat);
        converters.put(Float.class, Float::valueOf);

        converters.put(double.class, Double::parseDouble);
        converters.put(Double.class, Double::valueOf);

        converters.put(LocalDate.class, LocalDate::parse);
        converters.put(LocalTime.class, LocalTime::parse);
        converters.put(LocalDateTime.class, LocalDateTime::parse);
        converters.put(ZonedDateTime.class, ZonedDateTime::parse);
        converters.put(Duration.class, Duration::parse);
        converters.put(ZoneId.class, ZoneId::of);
    }
}
record PropertyExpr(String key, String defaultValue) {
}
