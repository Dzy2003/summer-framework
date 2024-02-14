package test_01.dao;



import com.duan.summer.annotations.Autowired;
import com.duan.summer.annotations.Bean;
import com.duan.summer.annotations.Configuration;
import com.duan.summer.summer.MapperScannerConfigurer;
import com.duan.summer.summer.SqlSessionFactoryBean;

import javax.sql.DataSource;


/**
 * mybatis的配置
 * 1.将jdbc中的DataSource注入到mybatis的sqlSessionFactoryBean，并设置别名包。
 * 2.设置mybatis的动态代理路径
 */

public class MyBatisConfig {

    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperPackage("mapper");
        return sqlSessionFactoryBean;
    }

    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("test_04.duan.mapper");
        return mapperScannerConfigurer;
    }
}
