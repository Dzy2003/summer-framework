package com.duan.summer.context;

import com.duan.summer.annotation.PropertySource;
import com.duan.summer.utils.ClassPathUtils;
import com.duan.summer.utils.YamlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 白日
 * @create 2024/1/16 22:28
 * @description
 */

public class ConfigLoader {
    FileConfigRegistry registry;
    public ConfigLoader(FileConfigRegistry registry) {
        this.registry = registry;
    }
    public void loadConfig(Class<?> configClass) {
        Map<String, Object> propertyConfig = getPropertyConfig(configClass);
        Map<String, Object> defaultConfig = getDefaultConfig();
        propertyConfig.putAll(defaultConfig);
        registry.registryFileConfig(propertyConfig);
    }

    private Map<String, Object> getPropertyConfig(Class<?> configClass) {
        Map<String, Object> configMap = new HashMap<>();
        PropertySource propertySource = configClass.getAnnotation(PropertySource.class);
        if(propertySource == null) return configMap;
        String[] value = propertySource.value();
        for (String s : value) {
            Properties properties = new Properties();
            try {
                properties.load(configClass.getClassLoader().getResourceAsStream(s));
            }catch (Exception e){
                throw new RuntimeException("资源加载错误");
            }
            properties.stringPropertyNames().forEach(name -> configMap.put(name,properties.get(name)));
        }
        return configMap;
    }

    private Map<String, Object> getDefaultConfig() {
        InputStream inputStream = ClassPathUtils.getContextClassLoader().getResourceAsStream("application.yml");
        try(inputStream) {
            if(inputStream != null){
                return YamlUtils.loadYamlAsPlainMap("application.yml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new HashMap<>();
    }


}
