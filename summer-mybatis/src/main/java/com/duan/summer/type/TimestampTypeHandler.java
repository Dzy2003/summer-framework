package com.duan.summer.type;

import java.sql.*;

/**
 * @author 白日
 * @create 2024/2/2 22:16
 * @description
 */

public class TimestampTypeHandler implements TypeHandler<Timestamp>{

    @Override
    public void setParameter(PreparedStatement ps, int i, Timestamp parameter) throws SQLException {
        ps.setTimestamp(i, parameter);
    }

    @Override
    public Timestamp getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getTimestamp(columnName);
    }

    @Override
    public Timestamp getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }
}
