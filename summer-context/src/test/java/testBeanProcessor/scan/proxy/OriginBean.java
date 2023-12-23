package testBeanProcessor.scan.proxy;


import com.duan.summer.annotation.Component;
import com.duan.summer.annotation.Value;

@Component
public class OriginBean {

    @Value("Scan App")
    public String name;

    public String version;

    @Value("v1.0")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return this.version;
    }
}
