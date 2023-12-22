package testPropertyResolver;

import com.duan.summer.io.PropertyResolver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 白日
 * @create 2023/12/8 19:23
 * @description
 */

public class test {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(test.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        PropertyResolver resolver = new PropertyResolver();
        resolver.registryPropertyResolver(properties);
        Object obj = resolver.getProperty("${jdbc.url}", String.class);
        System.out.println(obj);
        assert obj != null;
        System.out.println(obj.getClass());
        Object withDefaultValue = resolver.getProperty("${jdc.ur:1}", String.class);
        Object withoutDefaultValue = resolver.getProperty("${jdbc.url:dwdadwdawd}", String.class);
        System.out.println("withDefaultValue:" + withDefaultValue);
        System.out.print("withoutDefaultValue:" + withoutDefaultValue);
        System.out.println(resolver.getProperty("${jdbc.username}", String.class));
    }
}
