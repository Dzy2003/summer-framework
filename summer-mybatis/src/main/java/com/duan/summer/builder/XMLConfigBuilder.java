package com.duan.summer.builder;

import cn.hutool.core.lang.ClassScanner;
import com.duan.summer.datasource.DataSourceFactory;
import com.duan.summer.io.Resources;
import com.duan.summer.mapping.*;
import com.duan.summer.session.Configuration;
import com.duan.summer.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XMLConfigBuilder extends ConfigBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     *
     * @return Configuration
     */
    public Configuration parse() {
        try {
            // 解析映射器
            mapperElement(root.element("mappers"));
            environmentsElement(root.element("environments"));
            settingsElement(root.element("settings"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void settingsElement(Element settings) {
        for (Element element : settings.elements("setting")) {
            String settingName = element.attributeValue("name");
            String settingValue = element.attributeValue("value");
            switch (settingName){
                case "ignoreSuffix" -> configuration.getColumnMapping().setIgnoreSuffix(settingValue);
                case "ignorePrefix" -> configuration.getColumnMapping().setIgnorePrefix(settingValue);
                case "mapUnderscoreToCamelCase" -> configuration.getColumnMapping()
                        .setHumpMapping(Boolean.valueOf(settingValue));
            }
        }
    }

    private void environmentsElement(Element context) throws Exception {
        String environment = context.attributeValue("default");

        List<Element> environmentList = context.elements("environment");
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)) {
                // 事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            String mapperClass = e.attributeValue("class");
            if(resource != null && mapperClass == null){
                InputStream inputStream = Resources.getResourceAsStream(resource);
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, resource);
                xmlMapperBuilder.parse();
            }else if (resource == null && mapperClass != null) {
                Class<?> mapperInterface = Resources.classForName(mapperClass);
                configuration.addMapper(mapperInterface);
            }
        }
        List<Element> scanPackageElement = mappers.elements("package");
        for (Element element : scanPackageElement) {
            String scanPackage = element.attributeValue("name");
            for (Class<?> mapperInterface : ClassScanner.scanPackage(scanPackage)) {
                configuration.addMapper(mapperInterface);
            }
        }
    }
}