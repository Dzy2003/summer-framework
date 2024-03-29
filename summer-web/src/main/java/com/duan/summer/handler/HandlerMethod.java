package com.duan.summer.handler;

import com.duan.summer.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;

/**
 * @author 白日
 * @create 2024/2/27 12:57
 * @description
 */

public class HandlerMethod {
    protected Object handler;

    protected Class<?> type;

    protected Method method;
    protected String path = "";
    protected RequestType requestMethod;
    protected MethodParameter[] parameters;

    public final static Set<Class<? extends Annotation>> mappingAnnotations = Set.of(
            GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class);

    public HandlerMethod(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
        initHandlerMethod();
    }

    public HandlerMethod(HandlerMethod handlerMethod) {
        this.path = handlerMethod.getPath();
        this.requestMethod = handlerMethod.getRequestMethod();
        this.method = handlerMethod.getMethod();
        this.type = handlerMethod.getType();
        this.handler = handlerMethod.getHandler();
        this.parameters = handlerMethod.getParameters();
    }

    private void initHandlerMethod() {
        this.type = handler.getClass();
        processorAnnotation();
        Parameter[] parameters = method.getParameters();
        MethodParameter[] methodParameters = new MethodParameter[parameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            methodParameters[i] = new MethodParameter(parameters[i], i);
        }
        this.parameters = methodParameters;
    }

    private void processorAnnotation() {
        if(type.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = type.getAnnotation(RequestMapping.class);
            getPath(requestMapping.value());
        }
        if(method.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            this.requestMethod = requestMapping.requestMethod();
            getPath(requestMapping.value());
        }
        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if(mappingAnnotations.contains(annotation)){
                switch (annotationType.getName()){
                    case "getMapping" -> this.requestMethod = RequestType.GET;
                    case "postMapping" -> this.requestMethod = RequestType.POST;
                    case "putMapping" -> this.requestMethod = RequestType.PUT;
                    case "deleteMapping" -> this.requestMethod = RequestType.DELETE;
                }
                try {
                    path += "/" + annotationType.getMethod("value").invoke(annotation);
                }catch (Exception e){
                    throw new RuntimeException("获取路径失败",e);
                }
            }
        }
    }

    private void getPath(String path) {
        this.path += path.startsWith("/") ? path : "/" + path;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MethodParameter[] parameters) {
        this.parameters = parameters;
    }

    public RequestType getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestType requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
                "handler=" + handler +
                ", type=" + type +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", requestMethod=" + requestMethod +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
