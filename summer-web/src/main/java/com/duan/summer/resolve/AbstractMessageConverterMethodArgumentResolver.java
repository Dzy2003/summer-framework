package com.duan.summer.resolve;

import com.duan.summer.handler.MethodParameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @author 白日
 * @create 2024/3/12 16:49
 * @description
 */

public abstract class AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver{
    ObjectMapper objectMapper = new ObjectMapper();
    protected Object readArgFromRequestBody(HttpServletRequest request, Class<?> paramType, MethodParameter methodParameter) throws JsonProcessingException {
        String json = this.getJson(request);
        List<?> obj = parseJson(json, paramType);
        if(methodParameter.isList()) {
            return obj;
        }
        return obj.get(0);
    }

    private List<?> parseJson(String json, Class<?> paramType) throws JsonProcessingException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, paramType);
        return objectMapper.readValue(json, javaType);
    }

    public String getJson(HttpServletRequest request){
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            // 现在requestBody中包含了请求体的文本内容
            System.out.println("Request Body: " + requestBody);
        } catch (IOException e) {
            // 处理可能的IO异常
            throw new RuntimeException("read request body error");
        }


        return requestBody.toString();
    }
}
