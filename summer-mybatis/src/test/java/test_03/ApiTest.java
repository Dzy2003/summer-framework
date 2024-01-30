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

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

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

        // 3. 测试验证
        User res = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", res);
    }

}
