package com.duan.summer.builder;

import com.duan.summer.io.Resources;
import com.duan.summer.mapping.*;
import com.duan.summer.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 白日
 * @create 2024/2/5 14:06
 * @description
 */

public class XMLMapperBuilder extends ConfigBuilder{
    private Element element;
    private String resource;
    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }
    private XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }

    @Override
    public Configuration parse() throws ClassNotFoundException {
        String namespace = element.attributeValue("namespace");
        if(namespace == null || namespace.isEmpty()){
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }
        if(!configuration.isResourceLoaded(resource)){
            resultMapElements(element.elements("resultMap"));
            buildStatement((element.elements("select")), namespace);
            buildStatement((element.elements("update")), namespace);
            buildStatement((element.elements("delete")), namespace);
            buildStatement((element.elements("insert")), namespace);
            configuration.addLoadedResource(namespace);
            configuration.addMapper(Class.forName(namespace));
            return configuration;
        }
        return null;
    }

    private void resultMapElements(List<Element> resultMap) {
        for (Element resultMapElement : resultMap) {
            try {
                resultMapElement(resultMapElement);
            }catch (Exception e){
                throw new RuntimeException("构建resultMap失败，请检测resultMap的书写。" + e);
            }
        }
    }

    private void resultMapElement(Element element) throws ClassNotFoundException, NoSuchFieldException {
        String id = element.attributeValue("id");
        Class<?> ResultType = Class.forName(element.attributeValue("type"));
        List<ResultMapping> resultMappings = new ArrayList<>();
        List<Element> resultChildren = element.elements();
        for (Element resultChild : resultChildren) {
            ResultMapping resultMapping = buildResultMapping(resultChild, ResultType);
            if("id".equals(resultChild.getName())){
                resultMapping.setID(true);
            }
            resultMappings.add(resultMapping);
        }
        ResultMap resultMap = new ResultMap.Builder(configuration, id, ResultType, resultMappings).build();
        configuration.getColumnMapping().registerResultMap(resultMap);
    }

    private ResultMapping buildResultMapping(Element resultChild, Class<?> ResultType) throws NoSuchFieldException {
        String property = resultChild.attributeValue("property");
        Class<?> mappingType = ResultType.getDeclaredField(property).getType();
        return new ResultMapping
                .Builder()
                .column(resultChild.attributeValue("column"))
                .property(property)
                .javaType(mappingType)
                .configuration(configuration)
                .build();
    }

    private void buildStatement(List<Element> nodes, String namespace) {
        for (Element node : nodes) {
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String resultMapId = node.attributeValue("resultMap");
            if(resultType == null){
                resultType = configuration.getColumnMapping().getResultMap(resultMapId).getType().getName();
            }
            String sql = node.getText();
            String msId = namespace + "." + id;
            String nodeName = node.getName();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

            BoundSql boundSql = new BoundSql(sql,typeAliasRegistry.resolveAlias(parameterType), typeAliasRegistry.resolveAlias(resultType),configuration);

            MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql,resultMapId).build();
            // 添加解析 SQL
            configuration.addMappedStatement(mappedStatement);
        }
    }
}
