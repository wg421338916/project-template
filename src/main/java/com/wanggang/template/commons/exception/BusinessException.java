package com.wanggang.template.commons.exception;

import lombok.Getter;

/**
 * 业务异常，需要强制捕获
 *
 * @author 秦腾
 * @version 1.0.0
 * @date 2020/2/14 2:36 下午
 * @since 1.0.0
 */
public class BusinessException extends Exception implements ResponseException {
    private final Integer code;
    @Getter
    private final String message;

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BusinessException create(Integer code, String message) {
        return new BusinessException(code, message);
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
