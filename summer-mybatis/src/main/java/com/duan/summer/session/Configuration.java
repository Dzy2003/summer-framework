package com.duan.summer.session;


import com.duan.summer.binding.MapperRegistry;
import com.duan.summer.datasource.druid.DruidDataSourceFactory;
import com.duan.summer.executor.Executor;
import com.duan.summer.executor.SimpleExecutor;
import com.duan.summer.executor.resultset.DefaultResultSetHandler;
import com.duan.summer.executor.resultset.ResultSetHandler;
import com.duan.summer.executor.statement.PreparedStatementHandler;
import com.duan.summer.executor.statement.StatementHandler;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.mapping.Environment;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.transaction.Transaction;
import com.duan.summer.transaction.jdbc.JdbcTransactionFactory;
import com.duan.summer.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小傅哥，微信：fustack
 * @description 配置项
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class Configuration {

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    protected TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    protected Environment environment;

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object[] parameters, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameters, resultHandler, boundSql);
    }
}
