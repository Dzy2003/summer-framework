import com.duan.summer.annotations.PostMapping;
import com.duan.summer.web.AnnotationConfigWebApplicationContext;
import config.SpringConfig;
import config.SpringMvcConfig;
import controller.UserController;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 白日
 * @create 2024/2/24 0:34
 * @description
 */

public class test {
    public static void main(String[] args) {
        AnnotationConfigWebApplicationContext rootApplicationContext = new AnnotationConfigWebApplicationContext(SpringConfig.class);
        AnnotationConfigWebApplicationContext childApplicationContext = new AnnotationConfigWebApplicationContext();
        childApplicationContext.addComponentClasses(SpringMvcConfig.class);
        try {
            UserService userService = childApplicationContext.getBean(UserService.class);
        }catch (Exception e){
            System.out.println("刷新前找不到");
        }
        childApplicationContext.setParent(rootApplicationContext);
        childApplicationContext.refresh();
        UserController userController = childApplicationContext.getBean(UserController.class);
        userController.introduction();
    }
    @Test
    public void dad(){
        List<String> originalList = Arrays.asList("Java", "Python", "C++", "PHP");
        List<String> list = Arrays.asList("吃饭", "睡觉", "玩游戏", "打胶");
        List<List<String>> listList = Arrays.asList(originalList, list);
        System.out.println(new ArrayList<>(listList));
    }
}
