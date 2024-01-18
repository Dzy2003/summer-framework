package dao;

/**
 * @author 白日
 * @create 2024/1/18 14:02
 * @description
 */

public interface IUserDao {
    String queryUserName(String uid);

    Integer queryUserAge(String uid);
}
