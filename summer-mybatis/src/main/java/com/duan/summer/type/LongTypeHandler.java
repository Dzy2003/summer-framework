package com.duan.summer.type;

import com.alibaba.druid.sql.visitor.functions.Lpad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 白日
 * @create 2024/2/2 22:16
 * @description
 */

public class LongTypeHandler implements TypeHandler<Long>{
    @Override
    public void setParameter(PreparedStatement ps, int i, Long parameter) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getResult(ResultSet rs, String columnName) throws SQLException {
        return null;
    }

    @Override
    public Long getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }
}
