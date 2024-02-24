package test_03.po;

/**
 * @author 白日
 * @create 2024/1/31 23:10
 * @description
 */

public class User {
    private Integer userId;
    private String userName;
    private Integer userAge;

    @Override
    public String toString() {
        return "User1{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userAge=" + userAge +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
}
