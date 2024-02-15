package com.duan.summer.mapping;




import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeHandler;


public class ParameterMapping {

    private Configuration configuration;
    // property
    private String property;

    // javaType = int
    private Class<?> javaType;
    private int index;
    private TypeHandler<?> typeHandler;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }


    public void setTypeHandler(TypeHandler<?> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    @Override
    public String toString() {
        return "ParameterMapping{" +
                "configuration=" + configuration +
                ", property='" + property + '\'' +
                ", javaType=" + javaType +
                ", typeHandler=" + typeHandler +
                '}';
    }
// jdbcType=NUMERIC

    private ParameterMapping() {
    }

    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, Class<?> javaType, int index) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
            parameterMapping.index = index;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }
        public ParameterMapping build(){
            if(parameterMapping.typeHandler == null && parameterMapping.javaType != null){
                TypeHandler<?> handler = parameterMapping.configuration
                        .getTypeHandlerRegistry().getHandler(parameterMapping.javaType);
                parameterMapping.typeHandler = handler;
            }
            return parameterMapping;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }


}
