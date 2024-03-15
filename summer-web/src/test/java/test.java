import com.duan.summer.web.AnnotationConfigWebApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import config.Calculator;
import config.SpringConfig;
import config.SpringMvcConfig;
import controller.UserController;
import org.junit.jupiter.api.Test;
import service.UserService;

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
    public void dad() throws JsonProcessingException {
        Calculator calculator = new Calculator(); // 创建计算器对象
        int res = calculator.add(2).add(2).add(2).calculate(); // 计算 2+2+2
        System.out.println(res); // 6
        calculator.eliminate();//清零
        //calculator.res = 5; 错误，不允许访问隐藏字段
        System.out.println(calculator.calculate());// 0
    }
}
