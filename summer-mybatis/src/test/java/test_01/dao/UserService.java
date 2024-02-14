package test_01.dao;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Component;
import test_04.duan.mapper.UserMapper;

/**
 * @author 白日
 * @create 2024/2/14 21:54
 * @description
 */
@Component
public class UserService {
    @Autowired
    UserMapper userMapper;

    public test_03.po.User1 selectById(Long id){
        return userMapper.queryUserInfoById(id);
    }

}
