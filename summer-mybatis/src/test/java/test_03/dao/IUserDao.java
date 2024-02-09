package test_03.dao;

import com.duan.summer.annotation.Param;
import com.duan.summer.annotation.Select;
import test_03.po.User;
import test_03.po.User1;

import java.util.List;

public interface IUserDao {
    User1 queryUserInfoById(User1 user);
    List<User1> queryUsersInfoById(User1 user);
    Long countAge(Long minAge,Long maxAge);

}
