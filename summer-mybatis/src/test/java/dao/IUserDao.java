package dao;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Mapper;

/**
 * @author 白日
 * @create 2024/1/18 14:02
 * @description
 */
@Mapper
public interface IUserDao {
    String queryUserName(String uid);

    Integer queryUserAge(String uid);
}
