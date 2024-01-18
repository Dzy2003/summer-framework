package testBeanProcessor;

import com.duan.summer.utils.YamlUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author 白日
 * @create 2024/1/17 21:55
 * @description
 */

public class test {
    @Test
    public void testYaml(){
        Map<String, Object> stringObjectMap = YamlUtils.loadYamlAsPlainMap("application.yml");

    }

}
