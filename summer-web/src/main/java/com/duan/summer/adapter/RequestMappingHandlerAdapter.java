package com.duan.summer.adapter;

import com.duan.summer.convert.*;
import com.duan.summer.handler.HandlerMapping;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.ServletInvocableHandlerMethod;
import com.duan.summer.resolve.*;
import com.duan.summer.support.WebServletRequest;
import com.duan.summer.web.ModelAndView;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * @author 白日
 * @create 2024/3/6 9:56
 * @description
 */

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter{
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
    private final Map<Class<?>, Convent<?>> conventRegister = new HashMap<>();
    @PostConstruct
    public void afterPropertiesSet(){
        initConventRegister();
        if(this.returnValueHandlers == null){
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite()
                    .addResolvers(getDefaultArgumentResolvers());
        }
        if(this.returnValueHandlers == null) {
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite()
                    .addResolvers(getDefaultReturnValueHandlers());
        }

    }

    private void initConventRegister() {
        conventRegister.put(Long.class, new LongConvent());
        conventRegister.put(Integer.class, new IntegerConvent());
        conventRegister.put(Boolean.class, new BooleanConvent());
        conventRegister.put(Double.class, new DoubleConvent());
        conventRegister.put(Float.class, new FloatConvent());
        conventRegister.put(Date.class, new DoubleConvent());
        conventRegister.put(String.class, new StingConvent());
    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        ArrayList<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();
        // TODO 将实现的HandlerMethodArgumentResolver添加到List中
        handlerMethodArgumentResolvers.add(new RequestParamMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new PathVariableMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new SessionAttributeMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new RequestHeaderMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new RequestResponseBodyMethodProcessor());
        return handlerMethodArgumentResolvers;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.add(new RequestResponseBodyMethodProcessor());
        handlerMethodReturnValueHandlers.add(new ViewNameMethodReturnValueHandler());
        handlerMethodReturnValueHandlers.add(new ModelAndViewMethodReturnValueHandler());
        // TODO 将实现的HandlerMethodReturnValueHandler添加到List中
        return handlerMethodReturnValueHandlers;
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
                                          HandlerMethod handler) throws Exception{
        // 省略其它操作，直接调用HandlerMethod
        return invokeHandlerMethod(request, response, handler);
    }

    private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response,
                                             HandlerMethod handler) throws Exception {
        WebServletRequest webServletRequest = new WebServletRequest(request, response);
        ModelAndView modelAndView = new ModelAndView();
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(handler);
        if(this.argumentResolvers != null) handlerMethod.setMethodArgumentResolver(this.argumentResolvers);
        if(this.returnValueHandlers != null) handlerMethod.setReturnValueHandlers(this.returnValueHandlers);
        handlerMethod.invokeAndHandle(webServletRequest, modelAndView, conventRegister);
        return modelAndView;
    }
}
