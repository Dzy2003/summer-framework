package testResourceResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * @author 白日
 * @create 2023/12/6 18:27
 * @description
 */

public class Main {
    public static void main(String[] args) {
        String path = "com/duan/summer";
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URI resourceURI = resources.nextElement().toURI();
                System.out.println("Resource URI: " +
                        URLDecoder.decode(resourceURI.toString(), StandardCharsets.UTF_8));
                String uriStr = removeTrailingSlash(uriToString(resourceURI));
                String uriBaseStr = uriStr.substring(0, uriStr.length() - path.length());
                System.out.println(uriBaseStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    static String uriToString(URI uri) {
        return URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
    }

    static String removeTrailingSlash(String s) {
        if (s.endsWith("/") || s.endsWith("\\")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

}
