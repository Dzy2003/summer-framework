package com.duan.summer.handler;

import com.duan.summer.annotations.RequestParam;

import java.lang.reflect.Parameter;

/**
 * @author 白日
 * @create 2024/2/27 14:15
 * @description
 */

public class ParameterInfo {
    String name;
    Class<?> type;
    Boolean isRequired = true;
    String defaultValue = "";

    public ParameterInfo(Parameter parameter){
        this.name = parameter.getName();
        this.type = parameter.getType();
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if(requestParam != null){
            this.defaultValue = requestParam.defaultValue();
            this.isRequired = requestParam.Required();
        }
    }
}
