package com.duan.summer.web;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/3/6 9:43
 * @description
 */

public class ModelAndView {
    Map<String, Object> ModelMap = new HashMap<>();
    Object View;
    private Integer HttpStatusCode;

    public ModelAndView() {
    }
    public ModelAndView(Map<String, Object> modelMap, Object view, Integer httpStatusCode) {
        ModelMap = modelMap;
        View = view;
        HttpStatusCode = httpStatusCode;
    }

    public Map<String, Object> getModelMap() {
        return ModelMap;
    }

    public void setModelMap(Map<String, Object> modelMap) {
        ModelMap = modelMap;
    }

    public Object getView() {
        return View;
    }

    public void setView(Object view) {
        View = view;
    }

    public Integer getHttpStatusCode() {
        return HttpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        HttpStatusCode = httpStatusCode;
    }
}
