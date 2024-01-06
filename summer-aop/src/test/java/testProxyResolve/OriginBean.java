package testProxyResolve;

public class OriginBean implements ProxyInterface ,ProxyInterface1{

    public String name;

    @Polite
    public void hello() {
        System.out.println("hello," + name);
    }

    public String morning() {
        return "Morning, " + name + ".";
    }

    @Override
    @Polite
    public void hello1() {
        System.out.println("hello1," + name);
    }
}
