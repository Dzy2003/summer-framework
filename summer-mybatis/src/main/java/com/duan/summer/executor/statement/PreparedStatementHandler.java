package com.duan.summer.executor.statement;

import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.mapping.ParameterMapping;
import com.duan.summer.session.ResultHandler;
import com.duan.summer.type.TypeHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/30 13:10
 * @description
 */

public class PreparedStatementHandler extends BaseStatementHandler{
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object[] parameters) {
        super(executor, mappedStatement, parameters);
    }

    @Override
    protected Statement instantiateStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());

    }

    @Override
    public void parameterize(Statement statement) throws SQLException, IllegalAccessException {
        System.out.println("==>  Preparing:" + mappedStatement.getBoundSql().getSql().replace("\n","").replace("       ",""));
        Map<String, Object> parameterMap = parseParameters(parameters);
        PreparedStatement ps = (PreparedStatement) statement;
        StringBuilder sb = new StringBuilder();
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            Object setValue = getValue(parameterMapping.getProperty(), parameterMap);
            if(parameterMapping.getTypeHandler() == null){
                parameterMapping.setTypeHandler(
                        configuration.getTypeHandlerRegistry().getHandler(setValue.getClass()));
            }
            sb.append(setValue).append("(").append(setValue.getClass().getSimpleName()).append("), ");
            setParam(parameterMapping, ps, setValue);
        }
        System.out.println("==> Parameters: " + sb);

    }

    @NotNull
    private static Object getValue(String property, Map<String, Object> parameterMap) {
        Object setValue = parameterMap.get(property);
        if(setValue == null){
            throw new RuntimeException("找不到叫做" + property + "的参数");
        }
        return setValue;
    }

    private void setParam(ParameterMapping parameterMapping, PreparedStatement ps, Object value) throws SQLException {
        TypeHandler typeHandler = parameterMapping.getTypeHandler();
        typeHandler.setParameter(ps, parameterMapping.getIndex(), value);

    }

    private void setParam(PreparedStatement ps) throws SQLException {
        TypeHandler handler = configuration.getTypeHandlerRegistry().getHandler(parameters[0].getClass());
        handler.setParameter(ps, 1, parameters[0]);
        System.out.println("==> Parameters:"+parameters[0]  + "("
                +parameters[0].getClass().getSimpleName()+")");
    }

    private boolean isSingleParam() {
        return boundSql.getParameterMappings().size() == 1 &&
                configuration.getTypeHandlerRegistry().hasTypeHandler(parameters[0].getClass());
    }

    private Map<String, Object> parseParameters(Object[] parameters) throws IllegalAccessException {
        HashMap<String, Object> parameterMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if(configuration.getTypeHandlerRegistry().hasTypeHandler(parameter.getClass())){
                parameterMap.put(mappedStatement.getParameterName()[i], parameter);
            } else {
                for (Field field : parameter.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    parameterMap.put(field.getName(), field.get(parameter));
                }
            }
        }
        return parameterMap;
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }
}
