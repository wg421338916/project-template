package com.aegis.template.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson 序列化工具类，
 * <p>
 * 注：公司要求不让使用 fast json
 * 文档： https://www.yiibai.com/jackson/jackson_data_binding.html
 *
 * @author wg
 * @version 1.0
 * @date 2020/09/16 10:30
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class JacksonUtil {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();

    //序列化时候统一日期格式
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    //设置null时候不序列化(只针对对象属性)
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    //反序列化时，属性不存在的兼容处理
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    //单引号处理
    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, Boolean.TRUE);

    objectMapper.registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

  private JacksonUtil() {
  }

  public static List<Object> json2List(String jsonStr) {
    JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
    try {
      return objectMapper.readValue(jsonStr, javaType);
    } catch (IOException ex) {
      log.error("convertJson2List error", ex);
    }

    return Lists.newArrayList();
  }

  public static Map<String, Object> json2Map(String jsonStr) {
    MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
    try {
      return objectMapper.readValue(jsonStr, mapLikeType);
    } catch (IOException ex) {
      log.error("convertJson2Map error", ex);
    }

    return Maps.newHashMap();
  }

  public static <T> T json2Object(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException ex) {
      log.error("json2Object JsonProcessingException error", ex);
    }
    return null;
  }

  public static <T> T json2Object(String json, TypeReference<T> type) {
    try {
      return objectMapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      log.error("json2Object JsonProcessingException error", e);
    }

    return null;
  }

  public static <T> String object2Json(T entity) {
    try {
      return objectMapper.writeValueAsString(entity);
    } catch (JsonGenerationException e) {
      log.error("object2Json JsonGenerationException error", e);
    } catch (JsonMappingException e) {
      log.error("object2Json JsonMappingException error", e);
    } catch (IOException e) {
      log.error("object2Json IOException error", e);
    }

    return null;
  }
}
