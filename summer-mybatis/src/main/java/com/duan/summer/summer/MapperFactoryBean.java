package com.duan.summer.summer;

import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Bean;
import com.duan.summer.binding.MapperProxy;
import com.duan.summer.binding.MapperProxyFactory;
import com.duan.summer.session.Configuration;
import com.duan.summer.session.SqlSession;
import com.duan.summer.session.SqlSessionFactory;
import jakarta.annotation.PostConstruct;
import org.testng.Assert;

/**
 * @author 白日
 * @create 2024/2/12 23:19
 * @description Mapper代理对象的工厂bean
 */
@com.duan.summer.annotations.Configuration
public class MapperFactoryBean<T> {
    Class<T> mapperInterface;
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    @PostConstruct
    public void afterPropertiesSet(){
        Assert.assertNotNull(sqlSessionFactory,"sqlSessionFactory can not be null");
        Assert.assertNotNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = this.getSqlSession().getConfiguration();
        if(!configuration.hasMapper(mapperInterface)){
            configuration.addMapper(mapperInterface);
        }
    }
    @Bean
    public T getMapperProxy(){
        return getSqlSession().getMapper(mapperInterface);
    }
    private SqlSession getSqlSession(){
        return this.sqlSessionFactory.openSession();
    }
}
