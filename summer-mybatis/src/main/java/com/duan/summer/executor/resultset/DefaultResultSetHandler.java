package com.duan.summer.executor.resultset;

import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.*;
import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeHandler;
import com.duan.summer.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 白日
 * @create 2024/1/30 12:51
 * @description
 */

public class DefaultResultSetHandler implements ResultSetHandler{
    BoundSql boundSql;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;

    private final TypeHandlerRegistry typeHandlerRegistry;
    public DefaultResultSetHandler(MappedStatement mappedStatement){
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.boundSql = mappedStatement.getBoundSql();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }
    @Override
    public <T> List<T> handleResultSets(Statement stmt) throws SQLException {
        ResultSet resultSet = stmt.getResultSet();
        try {
            return (List<T>) resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            // 每次遍历行值
            while (resultSet.next()) {
                T obj = getRowValue(resultSet, clazz);
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("<==     " + mappedStatement.getSqlCommandType().name() +": " + list.size());
        return list;
    }

    private  <T> T getRowValue(ResultSet resultSet, Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException{
        if(configuration.getTypeHandlerRegistry().hasTypeHandler(clazz)){
            return configuration.getTypeHandlerRegistry().getHandler(clazz).getResult(resultSet, 1);
        }
        T instance = clazz.getConstructor().newInstance();
        // 自动映射：把每列的值都赋到对应的字段上
        applyAutomaticMappings(resultSet, configuration.getColumnMapping(), instance);
        // Map映射：根据映射类型赋值到字段
        applyPropertyMappings(resultSet, mappedStatement.getResultMap(), instance);
        return instance;

    }
    private <T> void applyAutomaticMappings(ResultSet resultSet, ColumnMapping columnMapping, T instance) throws SQLException, IllegalAccessException {
        String ignorePrefix = columnMapping.getIgnorePrefix();
        String ignoreSuffix = columnMapping.getIgnoreSuffix();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            String propertyName = getPropertyName(columnName, ignorePrefix, ignoreSuffix
                    , columnMapping.getHumpMapping());
            Field field;
            try {
                field = instance.getClass().getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            Object result = typeHandlerRegistry.getHandler(field.getType()).getResult(resultSet, columnName);
            field.setAccessible(true);
            field.set(instance, result);
        }

    }
    private <T> void applyPropertyMappings(ResultSet resultSet, ResultMap resultMap, T instance) throws SQLException, IllegalAccessException {
        if(resultMap == null) return;
        List<ResultMapping> resultMappings = resultMap.getResultMappings();
        for (ResultMapping resultMapping : resultMappings) {
            String property = resultMapping.getProperty();
            System.out.println(property);
            Object result;
            try {
                result = resultMapping.getTypeHandler().getResult(resultSet, resultMapping.getColumn());
            }catch (Exception e){
                continue;
            }
            Field field;
            try {
                field = resultMap.getType().getDeclaredField(property);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                continue;
            }
            field.set(instance, result);
        }
    }

    private String getPropertyName(String columnName, String ignorePrefix, String ignoreSuffix, Boolean isHumpMapping) {
        String res = columnName;
        if(columnName.startsWith(ignorePrefix)){
            res = columnName.substring(ignorePrefix.length());
        } else if (columnName.endsWith(ignoreSuffix)) {
            res = columnName.substring(0, columnName.length() - ignoreSuffix.length());
        }
        StringBuilder humpMapping = new StringBuilder();
        if(isHumpMapping){
            for (int i = 0; i < res.length(); i++) {
                char c = res.charAt(i);
                if(c != '_'){
                    humpMapping.append(c);
                }else if (i != res.length() - 1 && i != 0){
                    humpMapping.append(Character.toUpperCase(res.charAt(i+1)));
                    i++;
                }
            }
        }
        return isHumpMapping ? humpMapping.toString() : res;

    }

}
