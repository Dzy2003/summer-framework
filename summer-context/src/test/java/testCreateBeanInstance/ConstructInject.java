package testCreateBeanInstance;

import com.duan.summer.annotation.Autowired;
import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Value;

/**
 * @author 白日
 * @create 2023/12/18 18:17
 * @description
 */
@Component
public class ConstructInject {
    private final String field1;
    private final String Field2;
    private final DataSource dataSource;
    public ConstructInject(@Value("field1") String field1,
                           @Value("field2") String field2,
                           @Autowired DataSource dataSource) {
        this.field1 = field1;
        this.Field2 = field2;
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "ConstructInject{" +
                "field1='" + field1 + '\'' +
                ", Field2='" + Field2 + '\'' +
                ", dataSource=" + dataSource +
                '}';
    }
}
