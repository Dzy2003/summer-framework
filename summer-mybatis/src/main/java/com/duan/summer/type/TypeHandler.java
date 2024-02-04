package com.duan.summer.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 白日
 * @create 2024/2/2 21:09
 * @description
 */

public interface TypeHandler<T> {
    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    /**
     * 获取结果
     */
    T getResult(ResultSet rs, String columnName) throws SQLException;

    /**
     * 取得结果
     */
    T getResult(ResultSet rs, int columnIndex) throws SQLException;
}
