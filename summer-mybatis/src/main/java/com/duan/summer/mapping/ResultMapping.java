package com.duan.summer.mapping;


import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeHandler;
import com.duan.summer.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private TypeHandler<?> typeHandler;

    private Boolean isID;

    ResultMapping() {
    }

    public static class Builder {

        private ResultMapping resultMapping = new ResultMapping();

        public Builder() {

        }


        public Builder typeHandler(TypeHandler<?> typeHandler) {
            resultMapping.typeHandler = typeHandler;
            return this;
        }
        public Builder property(String property){
            this.resultMapping.property = property;
            return this;
        }
        public Builder column(String column){
            this.resultMapping.column = column;
            return this;
        }
        public Builder javaType(Class<?> javaType){
            this.resultMapping.javaType = javaType;
            return this;
        }
        public Builder column(Class<?> javaType){
            this.resultMapping.javaType = javaType;
            return this;
        }
        public Builder configuration(Configuration configuration){
            this.resultMapping.configuration = configuration;
            return this;
        }

        public ResultMapping build() {
            resolveTypeHandler();
            return resultMapping;
        }

        private void resolveTypeHandler() {
            if (resultMapping.typeHandler == null && resultMapping.javaType != null) {
                Configuration configuration = resultMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                resultMapping.typeHandler = typeHandlerRegistry.getHandler(resultMapping.javaType);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public String getColumn() {
        return column;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public void setTypeHandler(TypeHandler<?> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public Boolean getID() {
        return isID;
    }

    public void setID(Boolean ID) {
        isID = ID;
    }
}
