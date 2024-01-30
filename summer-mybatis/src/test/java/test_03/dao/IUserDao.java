package test_03.dao;

import com.duan.summer.annotation.Select;
import test_03.po.User;

public interface IUserDao {
    User queryUserInfoById(Long uId);

}
