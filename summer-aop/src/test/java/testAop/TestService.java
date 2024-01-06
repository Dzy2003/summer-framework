package testAop;

/**
 * @author 白日
 * @create 2024/1/6 16:00
 * @description
 */

public interface TestService {
    /**
     * 打印一句话  拦截方法
     * @param msg 返回信息
     */
    String print(String msg) throws InterruptedException;
    /**
     * 普通方法 不拦截
     **/
    void sendUser();
}
