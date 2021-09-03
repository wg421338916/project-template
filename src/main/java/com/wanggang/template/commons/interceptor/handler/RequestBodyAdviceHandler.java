package com.wanggang.template.commons.interceptor.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.wanggang.template.commons.annotation.DecryptRequest;
import com.wanggang.template.commons.constants.SystemCodeConstants;
import com.wanggang.template.commons.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 统一入参数处理
 * 参考： https://gitee.com/xxssyyyyssxx/common-crypto/tree/master/src/main/java/top/jfunc/common/crypto
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/8 16:56
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.8
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.wanggang.template.controller")
@RestController
public class RequestBodyAdviceHandler extends RequestBodyAdviceAdapter {

    @Value("${wanggang.encrypt.seed}")
    private String seed;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return decryptRequest(methodParameter);
    }

    private Boolean decryptRequest(MethodParameter returnType) {
        DecryptRequest annotation = AnnotationUtil.getAnnotation(returnType.getContainingClass(), DecryptRequest.class);
        if (annotation != null && annotation.value()) {
            return Boolean.TRUE;
        }

        annotation = AnnotationUtil.getAnnotation(returnType.getMethod(), DecryptRequest.class);
        if (annotation != null && annotation.value()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        if (decryptRequest(methodParameter).equals(Boolean.TRUE)) {
            return new HttpInputMessage() {
                @Override
                public HttpHeaders getHeaders() {
                    return httpInputMessage.getHeaders();
                }

                @Override
                public InputStream getBody() {
                    try {
                        String content = IoUtil.read(httpInputMessage.getBody(), StandardCharsets.UTF_8);
                        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), seed.getBytes(StandardCharsets.UTF_8)).getEncoded();
                        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

                        return new ByteArrayInputStream(symmetricCrypto.decrypt(content));
                    } catch (Exception ex) {
                        throw BaseException.create(SystemCodeConstants.C400);
                    }
                }
            };
        }

        return new MappingJacksonInputMessage(httpInputMessage.getBody(), httpInputMessage.getHeaders());
    }
}
