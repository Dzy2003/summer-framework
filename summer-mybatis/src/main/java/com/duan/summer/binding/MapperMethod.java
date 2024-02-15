package com.duan.summer.binding;

import com.duan.summer.annotations.Param;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.mapping.SqlCommandType;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;


public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        return switch (command.getType()) {
            case INSERT -> sqlSession.insert(command.getName(), args);
            case DELETE -> sqlSession.delete(command.getName(), args);
            case UPDATE -> sqlSession.update(command.getName(), args);
            case SELECT -> command.isResList ?
                    sqlSession.selectList(command.getName(), args) :
                    sqlSession.selectOne(command.getName(), args);
            default -> throw new RuntimeException("Unknown execution method for: " + command.getName());
        };
    }

    /**
     * SQL 指令
     */
    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;

        private final boolean isResList;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            String[] parameterNames = new String[method.getParameterCount()];
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if(parameters[i].isAnnotationPresent(Param.class)){
                    String value = parameters[i].getAnnotation(Param.class).value();
                    parameterNames[i] = value;
                }else {
                    parameterNames[i] = "param" + (i+1);
                }
            }
            ms.setParameterName(parameterNames);
            name = ms.getId();
            type = ms.getSqlCommandType();
            isResList = method.getReturnType().isAssignableFrom(List.class);
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
