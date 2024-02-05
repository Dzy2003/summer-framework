package test_03.po;

/**
 * @author 白日
 * @create 2024/1/31 23:10
 * @description
 */

public class User1 {
    private Integer uid;
    private String uname;
    private Integer uage;

    public void setUid(Integer uid) {
        this.uid = Integer.valueOf(uid);
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "User1{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", uage=" + uage +
                '}';
    }

    public void setUage(Integer uage) {
        this.uage = uage;
    }


}
