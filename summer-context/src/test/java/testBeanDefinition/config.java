package testBeanDefinition;

import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.ComponentScan;
import com.duan.summer.annotation.Import;
import com.duan.summer.utils.ClassUtils;

/**
 * @author 白日
 * @create 2023/12/10 22:12
 * @description
 */
@ComponentScan({"com.duan.summer.io","com.duan.summer.exception"})
@Import(ClassUtils.class)
public class config {
}
