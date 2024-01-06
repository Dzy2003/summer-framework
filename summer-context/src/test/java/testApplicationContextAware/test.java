package testApplicationContextAware;

import com.duan.summer.context_rebuild.AnnotationConfigApplicationContext;

/**
 * @author 白日
 * @create 2024/1/4 18:14
 * @description
 */

public class test {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ScanApplication.class)) {

        }
    }
}
