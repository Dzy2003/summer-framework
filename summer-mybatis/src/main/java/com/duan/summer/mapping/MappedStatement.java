package com.duan.summer.mapping;


import com.duan.summer.session.Configuration;

import java.util.Map;


public class MappedStatement {

    private Configuration configuration;
    private String id;
    private SqlCommandType sqlCommandType;

    private String[] ParameterName;
    private BoundSql boundSql;
    private ResultMap resultMap;

    public String[] getParameterName() {
        return ParameterName;
    }

    public void setParameterName(String[] parameterName) {
        ParameterName = parameterName;
    }



    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType,
                       BoundSql boundSql,String resultMapID) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
            mappedStatement.resultMap = configuration.getColumnMapping().getResultMap(resultMapID);
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public ResultMap getResultMap() {
        return resultMap;
    }

    public void setResultMap(ResultMap resultMap) {
        this.resultMap = resultMap;
    }
}
