package test_04.duan.mapper;

import com.duan.summer.annotations.Component;
import com.duan.summer.annotations.Param;
import test_03.po.User1;

import java.util.List;
@Component
public interface UserMapper {
    User1 queryUserInfoById(@Param("userId") Long userId);
    List<User1> queryUsersInfoById(User1 user,Long aa);
    Long countAge(Long minAge,Long maxAge);

}
