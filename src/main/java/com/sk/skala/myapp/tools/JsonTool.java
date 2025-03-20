package com.sk.skala.myapp.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonTool {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 객체를 JSON 문자열로 변환
    public static String toString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JsonTool.toString: {}", e.toString());
            return "";
        }
    }

    // JSON 문자열을 특정 클래스 객체로 변환
    public static <T> T toObject(String data, Class<T> cls) {
        try {
            return objectMapper.readValue(data, cls);
        } catch (JsonProcessingException e) {
            log.error("JsonTool.toObject: {}", e.toString());
            return null;
        }
    }

    // JSON 문자열을 Map<String, Object>으로 변환
    public static Map<String, Object> toMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("JsonTool.toMap: {}", e.toString());
            return null;
        }
    }

    // JSON 문자열을 특정 클래스 리스트로 변환
    public static <T> List<T> toList(String data, Class<T> cls) {
        try {
            return objectMapper.readValue(data, objectMapper.getTypeFactory().constructCollectionType(List.class, cls));
        } catch (JsonProcessingException e) {
            log.error("JsonTool.toList: {}", e.toString());
            return new ArrayList<>();
        }
    }

    // JSON 문자열을 JsonNode로 변환
    public static JsonNode toJsonNode(String data) {
        try {
            return objectMapper.readTree(data);
        } catch (JsonMappingException e) {
            log.error("JsonTool.toJsonNode: {}", e.toString());
        } catch (JsonProcessingException e) {
            log.error("JsonTool.toJsonNode: {}", e.toString());
        }
        return null;
    }
}
