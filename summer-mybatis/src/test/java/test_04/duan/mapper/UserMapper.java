package test_04.duan.mapper;

import com.duan.summer.annotations.Component;
import com.duan.summer.annotations.Param;
import test_03.po.User;

import java.util.List;
@Component
public interface UserMapper {
    User queryUserInfoById(@Param("userId") Long userId);
    List<User> queryUsersInfoById(User user);
    Long countAge(Long minAge,Long maxAge);

}
