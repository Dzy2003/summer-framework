package com.duan.summer.binding;

import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.mapping.SqlCommandType;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author 小傅哥，微信：fustack
 * @description 映射器方法
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        return switch (command.getType()) {
            case INSERT -> sqlSession.selectOne(command.getName(), args);
            case DELETE -> sqlSession.selectOne(command.getName(), args);
            case UPDATE -> sqlSession.selectOne(command.getName(), args);
            case SELECT -> sqlSession.selectOne(command.getName(), args);
            default -> throw new RuntimeException("Unknown execution method for: " + command.getName());
        };
    }

    /**
     * SQL 指令
     */
    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            name = ms.getId();
            type = ms.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
