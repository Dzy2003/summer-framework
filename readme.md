
# Summer框架

通过自己实现简化版本的SSM框架来熟悉SSM框架的源码和掌握基本原理以及巩固`JavaSe`基础和学习框架中的一些设计模式。

## 关于

Summer 框架是一个仿真的轻量级 JavaEE 开发框架，旨在模拟实现了 **Spring**、**SpringMVC**和 **MyBatis **的核心功能，提供了**IOC**、**AOP**、**半ORM**、**Web** 开发支持等特性，**基本实现了SSM的主要功能**。



## 模块介绍

### context模块

这是项目的核心模块，在这个模块实现了一个简单的IOC容器。在这里我们没有实现过时的XML配置而仅通过注解配置来实现Bean的注册，除此之外IOC容器还支持Yaml和XML格式的配置信息读取、BeanPostProcessor、BeanFactoryPostProcessor、Aware等扩展机制。

**过程文档**：

* [实现ResourceResolver](doc/01.实现ResourceResolver.md)
* [实现PropertyResolver](doc/02.实现PropertyResolver.md)
* [创建BeanDefinition](doc/03.创建BeanDefinition.md)
* [实例化Bean](doc/04.实例化Bean.md)
* [初始化Bean](doc/05.初始化Bean.md)
* [实现BeanPostProcessor](doc/06.实现BeanPostProcessor.md)

### AOP模块

基于**JDK**和**bytebuddy**的**动态代理技术**和IOC提供的**BeanPostProcessor**和**Aware**机制实现的AOP。使用了**责任链模式**和**回溯算法**解决了一个PointCut对应多个Advice的调用逻辑。

**过程文档**：

* [实现ProxyFactory](doc/07.实现ProxyFactory.md)
* [解析切面类并实现代理类逻辑.md](doc/08.解析切面类并实现代理类逻辑.md)

### Mybatis模块
基于JDK动态代理机制的半ORM框架，支持XML配置文件和注解开发。并且基于IOC的BeanFactoryPostProcessor等扩展机制实现了与IOC模块的整合。

**过程文档**：

* [搭建基础框架](doc/09.搭建基础框架.md)
* [搭建Sql执行器](doc/10.搭建Sql执行器.md)
* [Spring整合Mybatis的思路](doc/11.Spring整合Mybatis的思路.md)
* 实现整合

### Web模块
基于Servlet实现的Web框架。通过实现Servlet提供的ServletContainerInitializer接口加载载父子容器和DispatcherServlet，并使用FactoryBean机制初始化DispatcherServlet的各种组件如HandlerMapping、HandlerMappingAdapter等。

过程文档：

* [SpringMVC与Spring是如何联系的](doc/12.SpringMVC与Spring是如何联系的.md)
* .....(没写完)



## 使用方式

该框架与SSM框架的整合方式基本相同，可以参照之前SSM的整合方式来进行整合。



### 导入依赖

```xml
<dependency>
            <groupId>com.duan.summer</groupId>
            <artifactId>summer-context</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>com.duan</groupId>
            <artifactId>summer-web</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>com.duan</groupId>
            <artifactId>summer-mybatis</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

* 上面三个是整合需要的三个基本模块，如果需要AOP，可以再导入AOP模块。



### 创建数据库实体对象

``` java
package org.example.pojo;

/**
 * @author 白日
 * @create 2023/11/9 17:21
 */

public class Employee {
    private Integer id;

    private String name;

    private Integer age;

    private String position;

    //get、set、tostring....
}

```



### Mapper接口(注解实现)

```java
@Component
public interface EmployeeMapper {
    @Insert("INSERT INTO  employee (name, age, position) VALUES (#{name}, #{age}, #{position})")
    int insert(Employee employee);
    @Select("select * from employee where id = #{id}")
    Employee selectByID(@Param("id") Long id);

    @Delete("delete from employee where id = #{id}")
    int deleteByID(@Param("id") Integer id);

    @Update("update employee set name = #{name}, age = #{age}, position = #{position} where id = #{id}")
    int updateByID(Employee employee);
}
```

同样可支持XML配置文件开发Mapper



### Service注入Mapper代理类

```java
@Component
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    public Employee selectById(Long id){
        return employeeMapper.selectByID(id);
    }
}
```



### Controller注入Service层

```java
@Controller
@RequestMapping("employees")
public class EmployeeController {
    @Autowired
    EmployeeService service;
    @RequestMapping(value = "id", requestMethod = RequestType.GET)
    public Employee getEmployeeByID(@RequestParam("id") Long id) {
        return service.selectById(id);
    }
}
```



### 配置

1. 注册数据源到IOC容器

```java
@Configuration
public class JdbcConfig {
    @Bean
    public DataSource dataSource(@Value("${driver}") String driver,
                                 @Value("${url}")  String url,
                                 @Value("${username}") String username,
                                 @Value("${password}") String password){
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
```



2. 配置SqlSessionFactoryBean和MapperScannerConfigurer

```java
@Configuration
public class MyBatisConfig {
    //工厂模式启动SqlSession注册到IOC容器，支持忽略数据库字段前缀和驼峰映射等配置。
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //如果使用XML开发需要在这里设置Mapper包名 sqlSessionFactoryBean.setMapperPackage("mapper");
        return sqlSessionFactoryBean;
    }
    //支持注解开发，在创建Bean之前将Mapper接口的BeanDifination替换为MapperProxyFactory实现将接口替换为代理对象
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        //配置mapper扫描路径
        mapperScannerConfigurer.setBasePackage("org.example.mapper");
        return mapperScannerConfigurer;
    }
}
```



3. 



### 测试

tomcat正常启动后，我们使用PostMan测试该接口是否能够正常使用：

![image-20240318105700662](C:/Users/soga/AppData/Roaming/Typora/typora-user-images/image-20240318105700662.png)

可以看到PostMan正确以JSON格式返回了id为10的employees信息：

![image-20240318110017107](C:/Users/soga/AppData/Roaming/Typora/typora-user-images/image-20240318110017107.png)

再看后端日志输出：

``` java
10:58:29.226 [http-nio-8080-exec-8] INFO com.duan.summer.web.DispatcherServlet -- GET /testMVC_war_exploded/employees/id
10:58:29.236 [http-nio-8080-exec-8] DEBUG com.duan.summer.resolve.RequestParamMethodArgumentResolver -- Method getEmployeeByID index 0 param resolve: 10 by resolver RequestParamMethodArgumentResolver//参数处理器
10:58:29.311 [http-nio-8080-exec-8] INFO com.alibaba.druid.pool.DruidDataSource -- {dataSource-1} inited
==>  Preparing:select * from employee where id = ?
==> Parameters: 10(Long), 
<==      Total: 1
//输出日志，模仿了Mybatis框架输出的日志。
```

我们成功连同了数据库查询到了数据。

### 使用AOP记录日志

除了IOC，ORM，Web等特性，我们框架还实现了一个AOP的功能，下面我们介绍如何使用我们的AOP来记录日志：

1. 创建切面类：

```java
@Component
@Aspect
public class LogAspect {
    @Around(targetAnno = LogAnno.class)
    public Object around(ProceedingJoinPoint joinPoint) {
        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        System.out.println("调用" + joinPoint.getMethod().getName() + "方法，耗时：" + (System.currentTimeMillis() - begin));
        return proceed;
    }
}
```

* 目前仅支持Aroud类型的通知，并且通知的拦截规则仅支持注解，也就是会为标有`LogAnno`注解的方法或者类创建代理对象。

2. 对需要拦截的方法或者类打上`LogAnno`注解

```java
@LogAnno
public Employee selectById(Long id){
    return employeeMapper.selectByID(id);
}
```

3. 配置

我们需要确保`LogAspect`，切面类能够被扫描到，并且将代理核心类`AOPProxyFactory`注册到容器中：

```java
@Configuration
public class AopConfig {
    @Bean
    AOPProxyFactory createAroundProxyBeanPostProcessor() {
        return new AOPProxyFactory();
    }
}
```

4. 测试

``` bash
21:50:30.793 [http-nio-8080-exec-8] INFO com.alibaba.druid.pool.DruidDataSource -- {dataSource-1} inited
==>  Preparing:select * from employee where id = ?
==> Parameters: 10(Long), 
<==      Total: 1
调用selectById方法，耗时：317
```

可以看到，AOP生效成功记录了日志。

## 过程分析

