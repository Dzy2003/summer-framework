package com.duan.summer.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author 白日
 * @create 2023/12/6 18:21
 * @description 扫描指定包下的class文件
 */

public class ResourceResolver{
    Logger logger = LoggerFactory.getLogger(getClass());
    String basePackage;

    public ResourceResolver(String basePackage){
        this.basePackage = basePackage;
    }

    public <R> List<R> scan(Function<Resource, R> mapper) {
        String basePackagePath = this.basePackage.replace(".", "/");
        try {
            List<R> collector = new ArrayList<>();
            scan0(basePackagePath, collector, mapper);
            return collector;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    <R> void scan0(String basePackagePath,List<R> collector, Function<Resource, R> mapper)
            throws IOException, URISyntaxException {
        logger.info("scan path:{}" , basePackagePath);
        Enumeration<URL> resources = getContextClassLoader().getResources(basePackagePath);
        while (resources.hasMoreElements()){
            URI uri = resources.nextElement().toURI();
            //处理uri为字符串
            String uriStr = removeTrailingSlash(uriToString(uri));
            //拿到存放class文件的目录的路径
            String classPath = uriStr.substring(0, uriStr.length() - basePackagePath.length());
            //将“file:删除”
            if (classPath.startsWith("file:")) {
                classPath = classPath.substring(5);
            }
            if (uriStr.startsWith("jar:")) {
                scanFile(true, classPath, jarUriToPath(basePackagePath, uri), collector, mapper);
            } else {
                scanFile(false, classPath, Paths.get(uri), collector, mapper);
            }
        }
    }
    <R> void scanFile(boolean isJar, String classPath, Path root, List<R> collector,
                           Function<Resource, R> mapper) throws IOException {
        logger.info("classPath:{},root:{}", classPath, root);
        classPath = removeTrailingSlash(classPath);
        try(Stream<Path> pathStream = Files.walk(root)) {
            String finalClassPath = classPath;
            pathStream.filter(Files::isRegularFile)
                    .forEach(file -> {
                        Resource resource;
                        if(isJar){
                            resource = new Resource(finalClassPath, removeTrailingSlash(file.toString()));
                        }else {
                            String path = file.toString();
                            String name = removeLeadingSlash(path.substring(finalClassPath.length()));
                            resource = new Resource("file:" + path, name);
                        }
                        logger.atDebug().log("found resource: {}", resource);
                        R r = mapper.apply(resource);
                        if (r != null) {
                            collector.add(r);
                        }
                    });
        }
    }

    ClassLoader getContextClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) classLoader = getClass().getClassLoader();
        return classLoader;
    }

    String uriToString(URI uri) {
        return URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
    }
    String removeLeadingSlash(String s) {
        if (s.startsWith("/") || s.startsWith("\\")) {
            s = s.substring(1);
        }
        return s;
    }
    String removeTrailingSlash(String s) {
        if (s.endsWith("/") || s.endsWith("\\")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
    Path jarUriToPath(String basePackagePath, URI jarUri) throws IOException {
        return FileSystems.newFileSystem(jarUri, Map.of()).getPath(basePackagePath);
    }
}
