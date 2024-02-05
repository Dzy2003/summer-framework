package com.duan.summer.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 白日
 * @create 2024/2/5 13:31
 * @description
 */

public class ColumnMapping {
    private String prefix = "";
    private String suffix = "";
    private Map<String, ResultMap> resultMaps = new HashMap<>();
    private Boolean isHumpMapping = false;

    public ColumnMapping(){

    }
    public static class Builder{
        private final ColumnMapping columnMapping = new ColumnMapping();

        public Builder(String prefix, String suffix, Map<String, ResultMap> resultMaps,Boolean isHumpMapping){
            columnMapping.prefix = prefix;
            columnMapping.suffix = suffix;
            columnMapping.resultMaps = resultMaps;
            columnMapping.isHumpMapping = isHumpMapping;
        }
        public Builder(String prefix, String suffix, Map<String, ResultMap> resultMaps){
            columnMapping.prefix = prefix;
            columnMapping.suffix = suffix;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
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
