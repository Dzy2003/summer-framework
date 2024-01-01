package testProxyResolve;

public class OriginBean {

    public String name;

    @Polite
    public void hello() {
        System.out.println("hello," + name);
    }

    public String morning() {
        return "Morning, " + name + ".";
    }
}
