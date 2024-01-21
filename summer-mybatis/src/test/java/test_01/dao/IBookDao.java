package test_01.dao;

import com.duan.summer.annotation.Mapper;

/**
 * @author 白日
 * @create 2024/1/19 23:49
 * @description
 */
@Mapper
public interface IBookDao {
    String queryUserName(String uid);
}
