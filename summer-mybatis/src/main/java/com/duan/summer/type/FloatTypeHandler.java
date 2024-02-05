package com.duan.summer.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 白日
 * @create 2024/2/2 22:16
 * @description
 */

public class FloatTypeHandler implements TypeHandler<Float>{
    @Override
    public void setParameter(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getFloat(columnName);
    }

    @Override
    public Float getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }
}
