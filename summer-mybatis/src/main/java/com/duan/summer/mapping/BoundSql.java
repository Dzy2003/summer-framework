package com.duan.summer.mapping;

import java.util.List;
import java.util.Map;

/**
 * @author 小傅哥，微信：fustack
 * @description 绑定的SQL, 是从SqlSource而来，将动态内容都处理完成得到的SQL语句字符串，其中包括?,还有绑定的参数
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class BoundSql {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final String resultType;

    public BoundSql(String sql, List<ParameterMapping> parameterMappings, String resultType) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }


    public String getResultType() {
        return resultType;
    }
}
