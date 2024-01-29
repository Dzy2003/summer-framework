package test_03.dao;

import com.duan.summer.annotation.Select;

public interface IUserDao {
    @Select("SELECT id, userId, userHead, createTime\n" +
            "        FROM user\n" +
            "        where id = #{id}")
    String queryUserInfoById(String uId);

}
