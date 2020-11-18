package com.aegis.template.commons.interceptor.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.aegis.template.commons.annotation.EncryptResponse;
import com.aegis.template.commons.constants.SystemCodeConstants;
import com.aegis.template.commons.domain.Result;
import com.aegis.template.commons.domain.ResultPager;
import com.aegis.template.commons.exception.BaseException;
import com.aegis.template.commons.utils.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 统一返回值
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/8 16:56
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.8
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.aegis.template.controller")
@RestController
public class ResponseBodyAdviceHandler implements ResponseBodyAdvice<Object> {
  @Value("${aegis.encrypt.seed}")
  private String seed;

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
    return Boolean.TRUE;
  }

  @Override
  public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
    Result<Object> defaultResult = Result.success(null);
    if (data == null) {
      return defaultResult;
    }

    //如果返回值是Result或者ResultPager，且不加密
    Boolean encryptResponse = encryptResponse(returnType);
    Boolean supports = supports(data.getClass());
    if (Boolean.FALSE.equals(supports) && Boolean.FALSE.equals(encryptResponse)) {
      return data;
    }

    //获取返回值data部分对象
    Object dataT = data;
    if (Boolean.FALSE.equals(supports)) {
      Result<Serializable> result = (Result) data;
      dataT = result.getData();

      //如果返回值是result类型
      defaultResult.setCode(result.getCode());
      defaultResult.setMsg(result.getMsg());
    }

    if (dataT == null) {
      return defaultResult;
    }

    //如果加密
    if (Boolean.TRUE.equals(encryptResponse)) {
      byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), seed.getBytes(StandardCharsets.UTF_8)).getEncoded();
      SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
      dataT = symmetricCrypto.encryptBase64(JacksonUtil.object2Json(dataT));
    }

    defaultResult.setData((Serializable) dataT);

    //如果返回字符串对象，特殊处理
    if (returnType.getGenericParameterType().equals(String.class)) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        serverHttpResponse.getHeaders().add("content-type", MediaType.APPLICATION_JSON_VALUE);
        return objectMapper.writeValueAsString(defaultResult);
      } catch (Exception e) {
        log.error("统一返回值", e);
        throw BaseException.create(SystemCodeConstants.C500);
      }
    }

    return defaultResult;
  }

  private Boolean encryptResponse(MethodParameter returnType) {
    EncryptResponse annotation = AnnotationUtil.getAnnotation(returnType.getContainingClass(), EncryptResponse.class);
    if (annotation != null && annotation.value()) {
      return Boolean.TRUE;
    }

    annotation = AnnotationUtil.getAnnotation(returnType.getMethod(), EncryptResponse.class);
    if (annotation != null && annotation.value()) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  private Boolean supports(Type parameterType) {
    return !parameterType.equals(Result.class) && !parameterType.equals(ResultPager.class);
  }
}
