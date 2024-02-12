package com.duan.summer.builder.annotation;

import com.duan.summer.annotations.*;
import com.duan.summer.builder.ConfigBuilder;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.mapping.ParameterMapping;
import com.duan.summer.mapping.SqlCommandType;
import com.duan.summer.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author 白日
 * @create 2024/2/11 12:22
 * @description
 */

public class MapperAnnotationBuilder extends ConfigBuilder {
    private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<>();
    private Class<?> mapperInterface;

    public MapperAnnotationBuilder(Configuration configuration, Class<?> mapperInterface) {
        super(configuration);
        this.mapperInterface = mapperInterface;
        sqlAnnotationTypes.add(Select.class);
        sqlAnnotationTypes.add(Insert.class);
        sqlAnnotationTypes.add(Update.class);
        sqlAnnotationTypes.add(Delete.class);
    }

    @Override
    public Configuration parse()  {
        String resource = mapperInterface.getName();
        if (!configuration.isResourceLoaded(resource)) {
            for (Method method : mapperInterface.getMethods()) {
                parseStatement(method);
            }
        }

        return this.configuration;
    }

    private void parseStatement(Method method) {
        String id = mapperInterface.getName() + "." +method.getName();
        SqlCommandType sqlCommandType = getSqlCommandType(method);
        BoundSql boundSql = getBoundSql(method);
        String resultMapId = getResultMapId(method.getAnnotation(ResultMap.class));
        configuration.addMappedStatement(new MappedStatement.Builder(configuration, id, sqlCommandType, boundSql,resultMapId).build());
    }

    private String getResultMapId(ResultMap annotation) {
        if(annotation == null) return null;
        return annotation.value();
    }

    private BoundSql getBoundSql(Method method) {
        String sql = getSql(method);
        Class<?> resultType = getResultType(method);
        Class<?> parameterType = getParameterType(method);
        return new BoundSql(sql, parameterType, resultType, configuration);
    }

    private Class<?> getParameterType(Method method) {
        if(method.getParameters().length > 1){
            return null;
        }
        return method.getParameters()[0].getType();
    }

    private Class<?> getResultType(Method method) {
        Type returnType = method.getGenericReturnType();
        if(returnType instanceof ParameterizedType){
            try {
                return Class.forName(((ParameterizedType) returnType).getActualTypeArguments()[0].getTypeName());
            }catch (Exception e){
                throw new RuntimeException("");
            }
        }
        return method.getReturnType();
    }


    private String getSql(Method method) {
        try {
            Class<? extends Annotation> annotationType = getSqlAnnotationType(method);
            if(annotationType != null){
                Annotation annotation = method.getAnnotation(annotationType);
                return annotation.getClass().getMethod("value").invoke(annotation).toString();
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("Could not find value method on SQL annotation.  Cause: " + e);
        }
    }

    private SqlCommandType getSqlCommandType(Method method) {
        Class<? extends Annotation> type = getSqlAnnotationType(method);
        if (type == null) {
            return SqlCommandType.UNKNOWN;
        }
        return SqlCommandType.valueOf(type.getSimpleName().toUpperCase(Locale.ENGLISH));
    }
    private Class<? extends Annotation> getSqlAnnotationType(Method method) {
        for (Class<? extends Annotation> type : sqlAnnotationTypes) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) return type;
        }
        return null;
    }
}
