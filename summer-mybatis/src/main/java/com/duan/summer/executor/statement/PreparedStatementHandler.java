package com.duan.summer.executor.statement;

import com.duan.summer.executor.Executor;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.mapping.ParameterMapping;
import com.duan.summer.session.ResultHandler;
import com.duan.summer.type.TypeHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/30 13:10
 * @description
 */

public class PreparedStatementHandler extends BaseStatementHandler{
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object[] parameters, ResultHandler resultHandler) {
        super(executor, mappedStatement, parameters, resultHandler);
    }

    @Override
    protected Statement instantiateStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());

    }

    @Override
    public void parameterize(Statement statement) throws SQLException, IllegalAccessException {
        Map<String, Object> parameterMap = parseParameters(parameters);
        logger.debug("解析参数列表结果：" + parameterMap);
        PreparedStatement ps = (PreparedStatement) statement;
        if(boundSql.getParameterMappings().size() == 1 &&
                configuration.getTypeHandlerRegistry().hasTypeHandler(parameters[0].getClass())){
            logger.debug("解析参数：" + boundSql.getParameterMappings().get(0).getProperty() + " " + 0 + " "
                    + parameters[0]);
            TypeHandler handler = configuration.getTypeHandlerRegistry().getHandler(parameters[0].getClass());
            handler.setParameter(ps, 1, parameters[0]);
        }else {
            for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                Object parameterSet = parameterMap.get(parameterMapping.getProperty());
                if(parameterSet == null){
                    throw new RuntimeException("找不到叫做" + parameterMapping.getProperty() + "的参数");
                }
                System.out.println(parameterSet.getClass());
                if(parameterMapping.getTypeHandler() == null){
                    parameterMapping.setTypeHandler(
                            configuration.getTypeHandlerRegistry().getHandler(parameterSet.getClass()));
                }
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, parameterMapping.getIndex(), parameterSet);
                logger.debug("解析参数：" + parameterMapping.getProperty() + " " + parameterMapping.getIndex() + " " + parameterSet);
            }
        }
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
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }
}
