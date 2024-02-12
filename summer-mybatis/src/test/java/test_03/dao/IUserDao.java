package test_03.dao;

import com.duan.summer.annotations.Param;
import test_03.po.User1;

import java.util.List;

public interface IUserDao {
    User1 queryUserInfoById(@Param("userId") Long userId);
    List<User1> queryUsersInfoById(User1 user,Long aa);
    Long countAge(Long minAge,Long maxAge);

}
