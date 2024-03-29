package com.duan.summer.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;



public interface ResultSetHandler {

    <E> List<E> handleResultSets(Statement stmt) throws SQLException;

}
