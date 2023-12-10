package testBeanDefinition;

import com.duan.summer.context.AnnotationConfigApplicationContext;
import com.duan.summer.io.PropertyResolver;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author 白日
 * @create 2023/12/10 22:11
 * @description
 */

public class testScanForClassNames {

    @Test
    public void testScan() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/jdbc.properties"));
        PropertyResolver resolver = new PropertyResolver(properties);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(config.class,resolver);
        Set<String> set = context.scanForClassNames(config.class);
        System.out.println(set);
    }
}
