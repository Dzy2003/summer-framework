import com.duan.summer.handler.AbstractHandlerMapping;
import com.duan.summer.handler.RequestMappingHandlerMapping;
import com.duan.summer.utils.ClassUtils;
import jakarta.annotation.PostConstruct;

/**
 * @author 白日
 * @create 2024/2/28 14:40
 * @description
 */

public class test {
    public static void main(String[] args) {
        System.out.println(ClassUtils.findAnnotationMethod(RequestMappingHandlerMapping.class, PostConstruct.class));
    }
}
