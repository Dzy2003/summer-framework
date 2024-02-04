package com.duan.summer.builder;

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
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 小傅哥，微信：fustack
 * @description XML配置构建器，建造者模式，继承BaseBuilder
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
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
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
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
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            //命名空间
            String namespace = root.attributeValue("namespace");
            buildStatement((root.elements("select")), namespace);
            buildStatement((root.elements("update")), namespace);
            buildStatement((root.elements("delete")), namespace);
            buildStatement((root.elements("insert")), namespace);
            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

    private void buildStatement(List<Element> nodes, String namespace) {
        for (Element node : nodes) {
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String sql = node.getText();

            // ? 匹配
            List<ParameterMapping> parameterMappings = new ArrayList<>();
            Pattern pattern = Pattern.compile("(#\\{(.*?)})");
            Matcher matcher = pattern.matcher(sql);
            Class<?> parameterJavaType = null;
            if(parameterType != null){
                try {
                    parameterJavaType = Class.forName(parameterType);
                }catch (ClassNotFoundException e){
                    throw new RuntimeException(e + "the parameterType" + parameterType +"is not find");
                }
            }
            for (int i = 1; matcher.find(); i++) {
                ParameterMapping parameterMapping = null;
                String g1 = matcher.group(1); //#{name}
                String property = matcher.group(2);
                sql = sql.replace(g1, "?");
                //第一种情况：只有一个基本类型参数
                if(configuration.getTypeHandlerRegistry().hasTypeHandler(parameterJavaType)){
                    parameterMapping = new ParameterMapping.Builder
                            (configuration,property, parameterJavaType,i).build();
                //第二种情况：没有参数类型，说明有多个非pojo类型参数
                } else if (parameterJavaType == null) {
                    parameterMapping = new ParameterMapping.Builder(configuration, property, null,i).build();
                    //第三种情况，用户定义pojo类型
                } else{
                    try {
                        parameterMapping = new ParameterMapping.Builder(configuration, property, parameterJavaType
                                .getDeclaredField(property).getType(),i).build();
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException("the #{" + property + "}" + " is not find in " + parameterJavaType);
                    }
                }
                System.out.println("构建parameterMapping：" + parameterMapping);
                parameterMappings.add(parameterMapping);
            }
            String msId = namespace + "." + id;
            String nodeName = node.getName();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

            BoundSql boundSql = new BoundSql(sql, parameterMappings, resultType);

            MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
            // 添加解析 SQL
            configuration.addMappedStatement(mappedStatement);
        }
    }
}