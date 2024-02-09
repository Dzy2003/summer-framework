package test_03;

import com.duan.summer.io.Resources;
import com.duan.summer.mapping.BoundSql;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_03.dao.IUserDao;
import test_03.po.User;
import test_03.po.User1;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 小傅哥，微信：fustack
 * @description 单元测试
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        User1 user1 = new User1();
        user1.setUserId(1);
        // 3. 测试验证
        User1 res = userDao.queryUserInfoById(user1);
        User1 user2 = new User1();
        user2.setUserAge(20);
        List<User1> user1List = userDao.queryUsersInfoById(user2);
        Long nums = userDao.countAge(18L, 20L);
        logger.info("测试结果：{},{},{}", res, user1List,nums);
    }

    public List<User> selectUserList() {
        return null;
    }

    @Test
    public void test() throws NoSuchMethodException, ClassNotFoundException {
        String resolveIgnore = "_user_name";
        StringBuilder resolveHumpMapping = new StringBuilder();
        for (int i = 0; i < resolveIgnore.length(); i++) {
            char c = resolveIgnore.charAt(i);
            if(c != '_'){
                resolveHumpMapping.append(c);
            }else if (i != resolveIgnore.length() - 1 && i != 0){
                resolveHumpMapping.append(Character.toUpperCase(resolveIgnore.charAt(i+1)));
                i++;
            }
        }
        System.out.println(resolveHumpMapping);

    }

}
