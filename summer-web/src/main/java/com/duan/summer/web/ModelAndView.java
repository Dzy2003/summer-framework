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

    String viewName;
    private Integer HttpStatusCode;

    private Boolean isRedirect;

    private boolean isRest = false;

    public ModelAndView() {
    }
    public ModelAndView(Map<String, Object> modelMap, Object view, Integer httpStatusCode) {
        ModelMap = modelMap;
        View = view;
        HttpStatusCode = httpStatusCode;
    }

    public void isRedirect(Boolean redirect) {
        isRedirect = redirect;
    }

    public boolean isRest() {
        return isRest;
    }

    public String getViewName() {
        return viewName;
    }

    public Boolean getRedirect() {
        return isRedirect;
    }

    public void addModel(String key, Object value) {
        ModelMap.put(key, value);
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setRest(boolean rest) {
        isRest = rest;
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

    @Override
    public String toString() {
        return "ModelAndView{" +
                "ModelMap=" + ModelMap +
                ", View=" + View +
                ", viewName='" + viewName + '\'' +
                ", HttpStatusCode=" + HttpStatusCode +
                ", isRedirect=" + isRedirect +
                ", isRest=" + isRest +
                '}';
    }
}
