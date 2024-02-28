package com.duan.summer.summer;

import cn.hutool.core.lang.Assert;
import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Value;
import com.duan.summer.builder.XMLConfigBuilder;
import com.duan.summer.builder.XMLMapperBuilder;
import com.duan.summer.io.Resources;
import com.duan.summer.mapping.Environment;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSessionFactory;
import com.duan.summer.session.SqlSessionFactoryBuilder;
import com.duan.summer.transaction.TransactionFactory;
import com.duan.summer.transaction.jdbc.JdbcTransactionFactory;
import jakarta.annotation.PostConstruct;
import org.dom4j.DocumentException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

/**
 * @author 白日
 * @create 2024/2/12 23:18
 * @description 构建SqlSession的工厂Bean
 */
@com.duan.summer.annotations.Configuration
public class SqlSessionFactoryBean {
    private Configuration configuration;
    private String environment;
    private TransactionFactory transactionFactory;

    private final SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    private SqlSessionFactory sqlSessionFactory;
    @Value("${mybatis.configLocation}")
    private String configLocation;
    @Value("${mybatis.mapperPackage}")
    private String mapperPackage;
    private DataSource dataSource;
    @Value("${mybatis.ignorePrefix:}")
    private String ignorePrefix;
    @Value("${mybatis.ignoreSuffix:}")
    private String ignoreSuffix;
    @Value("${mybatis.mapUnderscoreToCamelCase:}")
    private Boolean mapUnderscoreToCamelCase;
    public SqlSessionFactoryBean() {
    }
    @PostConstruct
    public void afterPropertiesSet()  {
        Assert.notNull(this.dataSource, "Property 'dataSource' is required");
        Assert.notNull(this.sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
        Assert.state(this.configuration == null && this.configLocation == null || this.configuration == null || this.configLocation == null, "Property 'configuration' and 'configLocation' can not specified with together");
        this.sqlSessionFactory = this.buildSqlSessionFactory();
    }

    private SqlSessionFactory buildSqlSessionFactory() {
        XMLConfigBuilder xmlConfigBuilder = null;
        Configuration configuration;
        if(this.configuration != null){
            configuration = this.configuration;
        }else if(this.configLocation != null){
            Reader reader = Resources.getResourceAsReader(configLocation);
            xmlConfigBuilder = new XMLConfigBuilder(reader);
            configuration = xmlConfigBuilder.getConfiguration();
        }else {
            configuration = new Configuration();
        }
        if(xmlConfigBuilder != null){
            try {
                xmlConfigBuilder.parse();
            }catch (Exception e){
                throw new RuntimeException("Failed to parse config resource: " + this.configLocation);
            }
        }

        if(this.transactionFactory == null){
            this.transactionFactory = new JdbcTransactionFactory();
        }
        configuration.setEnvironment(new Environment(environment,transactionFactory,dataSource));
        if(mapperPackage != null){
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Enumeration<URL> resources = contextClassLoader.getResources(mapperPackage);
                while (resources.hasMoreElements()){
                    URI uri = resources.nextElement().toURI();
                    Files
                            .walk(Paths.get(uri))
                            .filter(this::IsXMLFile)
                            .forEach(path -> {
                                        String resource = mapperPackage + "/" + path.getFileName();
                                try {
                                    new XMLMapperBuilder(contextClassLoader.getResourceAsStream(resource)
                                            , configuration, resource).parse();
                                } catch (ClassNotFoundException | DocumentException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                }
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("扫描XML包构建Mapper失败");
            }

        }
        return sqlSessionFactoryBuilder.build(configuration);

    }

    private Boolean IsXMLFile(Path path) {
        return path.subpath(path.getNameCount() - 1, path.getNameCount()) .toString().endsWith(".xml");
    }
    @Bean
    public SqlSessionFactory getSqlSessionFactory(@Autowired DataSource dataSource){
        this.dataSource = dataSource;
        if(this.sqlSessionFactory == null){
            this.afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
