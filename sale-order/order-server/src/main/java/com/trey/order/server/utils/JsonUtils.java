package com.trey.order.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为json字符串
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObject(String src, Class<T> clazz) {
        if(StringUtils.isEmpty(src) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) src : objectMapper.readValue(src, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
