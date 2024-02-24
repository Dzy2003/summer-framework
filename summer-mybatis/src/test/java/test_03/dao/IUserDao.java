package test_03.dao;

import com.duan.summer.annotations.Param;
import test_03.po.User;

import java.util.List;

public interface IUserDao {
    User queryUserInfoById(@Param("userId") Long userId);
    List<User> queryUsersInfoById(User user, Long aa);
    Long countAge(Long minAge,Long maxAge);

}
