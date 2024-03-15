package com.duan.summer.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author 白日
 * @create 2024/2/27 14:15
 * @description
 */

public class MethodParameter {
    Parameter parameter;
    int parameterIndex;
    String parameterName;
    Class<?> parameterType;
    Annotation[] parameterAnnotations;
    Class<?> GenericClass;
    Boolean isList;

    public MethodParameter(Parameter parameter, Integer parameterIndex){
        this.parameterName = parameter.getName();
        this.parameterType = parameter.getType();
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
        this.parameterAnnotations = parameter.getAnnotations();
        Type parameterizedType = parameter.getParameterizedType();
        isList = List.class.isAssignableFrom(parameterType);
        if(parameterizedType instanceof ParameterizedType){
            GenericClass = (Class<?>)((ParameterizedType) parameterizedType)
                    .getActualTypeArguments()[0];
        }
    }

    public Boolean isList() {
        return isList;
    }

    public void setList(Boolean list) {
        isList = list;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(Annotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

    public Class<?> getGenericClass() {
        return GenericClass;
    }

    public void setGenericClass(Class<?> genericClass) {
        GenericClass = genericClass;
    }

    public <A extends Annotation> A getParameterAnnotation(Class<A> anno) {
        return this.getParameter().getAnnotation(anno);
    }
}
