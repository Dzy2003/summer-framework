package com.duan.summer.handler;

import com.duan.summer.context.ApplicationContext;
import com.duan.summer.context.ApplicationContextAware;
import com.duan.summer.web.WebApplicationContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 白日
 * @create 2024/2/27 13:04
 * @description
 */

public abstract class AbstractHandlerMapping implements HandlerMapping, ApplicationContextAware {
    WebApplicationContext context;
    Logger logger = LoggerFactory.getLogger(getClass());
    public MappingRegistry mappingRegistry = new MappingRegistry();
    @PostConstruct
    public void afterPropertySet(){
        logger.debug("HandlerMapping初始化");
        initHandlerMethods();
    }

    @Override
    public void setApplication(ApplicationContext application) {
        this.context = (WebApplicationContext) application;
    }
    @Override
    public MappingRegistry getMappingRegistry() {
        return this.mappingRegistry;
    }

    protected void initHandlerMethods() {
        List<Object> allBeans = context.getAllBeans();
        allBeans.stream().forEach(this::processCandidateBean);
    }

    private void processCandidateBean(Object bean) {
        Class<?> beanType = null;
        try {
            beanType = bean.getClass();
        }catch (Exception e){
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Could not resolve type for bean '" + bean.getClass().getName() + "'", e);
            }
        }
        if(beanType != null && this.isHandler(beanType)){
            this.detectHandlerMethods(bean);
        }
    }

    protected void detectHandlerMethods(Object handler) {
        Class<?> handlerType = handler.getClass();
        Arrays.stream(handlerType.getDeclaredMethods())
                .map(method -> getMappingForMethod(method, handler))
                .forEach(this::registerHandlerMethod);
    }

    protected void registerHandlerMethod(HandlerMethod handlerMethod) {
        this.mappingRegistry.register(handlerMethod);
    }

    protected abstract HandlerMethod getMappingForMethod(Method method, Object handler);

    protected abstract boolean isHandler(Class<?> beanType);

    public class MappingRegistry{
        Map<String, Set<HandlerMethod>> MatchingPath = new HashMap<>();
        Map<String, Set<HandlerMethod>> containsPathVariableMatchingPath = new HashMap<>();

        void register(HandlerMethod handlerMethod){
            String path = handlerMethod.getPath();
            if (path.contains("{") && path.contains("}")){
                // /order/get/{id} -> /order/get/1
                path = path.replaceAll("\\{\\w+\\}", "(\\\\w+)");
                register(containsPathVariableMatchingPath,path,handlerMethod);
            }else {
                // 根据请求路径的不同分别保存HandlerMethod
                register(MatchingPath,path,handlerMethod);
            }
        }

        private void register(Map<String, Set<HandlerMethod>> mapPath, String path, HandlerMethod handlerMethod) {
            // /order/get/{id} -> /order/get/1

            // 存在,可能请求类型一样 重复了
            if (mapPath.containsKey(path) && mapPath.get(path).contains(handlerMethod)) {
                throw new RuntimeException(handlerMethod.requestMethod.name() + handlerMethod.getPath() +"HandlerMethod相同");
            }
            if (!mapPath.containsKey(path)) {
                mapPath.put(path,new HashSet<>());
            }
            mapPath.get(path).add(handlerMethod);
        }

        @Override
        public String toString() {
            return "MappingRegistry{" +
                    "MatchingPath=" + MatchingPath +
                    ", containsPathVariableMatchingPath=" + containsPathVariableMatchingPath +
                    '}';
        }
    }
}
