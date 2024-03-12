package com.duan.summer.resolve;

import com.duan.summer.convert.Convent;
import com.duan.summer.handler.HandlerMethod;
import com.duan.summer.handler.MethodParameter;
import com.duan.summer.support.WebServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 白日
 * @create 2024/3/7 22:13
 * @description
 */

public abstract class AbstractNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver{
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest, Convent<?> convent) throws Exception {
        NamedValueInfo namedValueInfo = this.getNamedValueInfo(parameter);
        Object arg = null;
        try {
            arg = resolveName(namedValueInfo.name(),handlerMethod, webServletRequest, convent);
        }catch (Exception e){
            throw new RuntimeException("resolve parameter" +
                    namedValueInfo.name + "error in " +handlerMethod.getHandler().getClass().getName() +
                    "." + handlerMethod.getMethod().getName());
        }

        if(arg == null) {
            if(namedValueInfo.defaultValue != null){
                arg = convent.convent(namedValueInfo.defaultValue);
            }else if(namedValueInfo.required){
                throw new IllegalArgumentException("参数[" + namedValueInfo.name() + "]不能为空");
            }
        }
        resolveArgumentName();
        logger.debug("Method {} index {} param resolve: {} by resolver {}",
                handlerMethod.getMethod().getName(), parameter.getParameterIndex(), arg, this.getClass().getSimpleName());
        return arg;
    }

    protected abstract Object resolveName(String parameterName, HandlerMethod handlerMethod, WebServletRequest webServletRequest, Convent<?> convent);

    protected abstract NamedValueInfo getNamedValueInfo(MethodParameter parameter);

    protected abstract void resolveArgumentName();

    protected record NamedValueInfo(String name, String defaultValue, Boolean required){

    }
}
