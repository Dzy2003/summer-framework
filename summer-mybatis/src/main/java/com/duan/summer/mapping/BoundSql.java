package com.duan.summer.mapping;

import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoundSql {

    private String sql;
    private final List<ParameterMapping> parameterMappings = new ArrayList<>();
    private final Class<?> resultType;
    Configuration configuration;

    public BoundSql(String sql, Class<?> parameterType, Class<?> resultType, Configuration configuration) {
        this.sql = sql;
        this.resultType = resultType;
        this.configuration = configuration;
        generateParameterMappings(parameterType);
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }


    public Class<?> getResultType() {
        return resultType;
    }

    private void generateParameterMappings(Class<?> parameterJavaType){
        // ? 匹配
        Pattern pattern = Pattern.compile("(#\\{(.*?)})");
        Matcher matcher = pattern.matcher(sql);
        String newSql = "";
        for (int i = 1; matcher.find(); i++) {
            ParameterMapping parameterMapping = null;
            String g1 = matcher.group(1); //#{name}
            String property = matcher.group(2);
            newSql = sql.replace(g1, "?");
            //第一种情况：只有一个基本类型参数
            if(configuration.getTypeHandlerRegistry().hasTypeHandler(parameterJavaType)){
                parameterMapping = new ParameterMapping.Builder
                        (configuration ,property, parameterJavaType,i).build();
                //第二种情况：没有参数类型，说明有多个参数
            } else if (parameterJavaType == null) {
                parameterMapping = new ParameterMapping.Builder(configuration, property, null,i).build();
                //第三种情况，用户定义pojo类型
            } else{
                try {
                    parameterMapping = new ParameterMapping.Builder(configuration, property, parameterJavaType
                            .getDeclaredField(property).getType(),i).build();
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("the #{" + property + "}" + " is not find in " + parameterJavaType);
                }
            }
            parameterMappings.add(parameterMapping);
            this.sql = newSql;
        }
    }
}
