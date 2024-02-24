package test_03;

import com.duan.summer.io.Resources;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_03.dao.IUserDao;
import test_03.po.User;
import test_04.duan.mapper.UserMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

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
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 3. 测试验证
        User res = userMapper.queryUserInfoById(1L);
        User user2 = new User();
        user2.setUserAge(20);
        List<User> user1List = userMapper.queryUsersInfoById(user2);
        Long nums = userMapper.countAge(18L, 20L);
        logger.info("测试结果：{},{},{}", res, user1List,nums);
    }

    public List<User> selectUserList() {
        return null;
    }

    @Test
    public void test() throws NoSuchMethodException, ClassNotFoundException, IOException, URISyntaxException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = contextClassLoader.getResources("mapper");
        while (resources.hasMoreElements()){
            URI uri = resources.nextElement().toURI();
            List<InputStream> list = Files.walk(Paths.get(uri)).map(path -> {
                if(path.subpath(path.getNameCount() - 1, path.getNameCount()) .toString().endsWith(".xml")){
                    System.out.println("mapper/" + path.getFileName());
                    return contextClassLoader.getResourceAsStream("mapper/" + path.getFileName());
                }
                return null;
            }).toList();
            list.forEach(inputStream -> {

            });
        }
    }

}
