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
import com.duan.summer.mapping.ColumnMapping;
import com.duan.summer.mapping.Environment;
import com.duan.summer.mapping.MappedStatement;
import com.duan.summer.transaction.Transaction;
import com.duan.summer.transaction.jdbc.JdbcTransactionFactory;
import com.duan.summer.type.TypeAliasRegistry;
import com.duan.summer.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    protected TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    protected Environment environment;

    protected ColumnMapping columnMapping = new ColumnMapping();
    protected final Set<String> loadedResources = new HashSet<>();


    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
    }
    public ColumnMapping getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(ColumnMapping columnMapping) {
        this.columnMapping = columnMapping;
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

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
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
    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }
    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(MappedStatement mappedStatement) {
        return new DefaultResultSetHandler(mappedStatement);
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
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object[] parameters) {
        return new PreparedStatementHandler(executor, mappedStatement, parameters);
    }
}
