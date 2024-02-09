package com.duan.summer.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/2/5 13:31
 * @description
 */

public class ColumnMapping {
    private String ignorePrefix = "";
    private String ignoreSuffix = "";
    private Map<String, ResultMap> resultMaps = new HashMap<>();
    private Boolean isHumpMapping = false;

    public ColumnMapping(){

    }
    public static class Builder{
        private final ColumnMapping columnMapping = new ColumnMapping();

        public Builder(String prefix, String suffix, Map<String, ResultMap> resultMaps,Boolean isHumpMapping){
            columnMapping.ignorePrefix = prefix;
            columnMapping.ignoreSuffix = suffix;
            columnMapping.resultMaps = resultMaps;
            columnMapping.isHumpMapping = isHumpMapping;
        }
        public Builder(String prefix, String suffix, Map<String, ResultMap> resultMaps){
            columnMapping.ignorePrefix = prefix;
            columnMapping.ignoreSuffix = suffix;
            columnMapping.resultMaps = resultMaps;
        }
        public ColumnMapping build(){
            return this.columnMapping;
        }
    }
    public void registerResultMap(ResultMap resultMap){
        this.resultMaps.put(resultMap.getId(), resultMap);
    }
    public ResultMap getResultMap(String id){
        return this.resultMaps.get(id);
    }

    public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(String prefix) {
        this.ignorePrefix = prefix;
    }

    public String getIgnoreSuffix() {
        return ignoreSuffix;
    }

    public void setIgnoreSuffix(String suffix) {
        this.ignoreSuffix = suffix;
    }

    public Map<String, ResultMap> getResultMaps() {
        return resultMaps;
    }

    public void setResultMaps(Map<String, ResultMap> resultMaps) {
        this.resultMaps = resultMaps;
    }

    public Boolean getHumpMapping() {
        return isHumpMapping;
    }

    public void setHumpMapping(Boolean humpMapping) {
        isHumpMapping = humpMapping;
    }
}
