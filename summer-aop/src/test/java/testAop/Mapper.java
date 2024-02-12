package testAop;

import com.duan.summer.annotations.Component;

/**
 * @author 白日
 * @create 2024/1/11 23:44
 * @description
 */
@Component
public class Mapper {
    @DAO
    public void select(){
        System.out.println("select");
    }
}
