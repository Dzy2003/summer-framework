package testBeanDefinition;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.annotation.Import;
import com.duan.summer.annotation.PropertySource;
import com.duan.summer.utils.ClassUtils;

/**
 * @author 白日
 * @create 2023/12/10 22:12
 * @description
 */
@ComponentScan({"testBeanDefinition"})
@Import(ClassUtils.class)
@PropertySource("jdbc.properties")
public class config {
}
