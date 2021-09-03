package com.wanggang.template.commons.exception;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 验证码验证失败异常
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 11:07
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class VerificationCodeErrorException extends AuthenticationException implements ResponseException {
    @Getter
    protected final Integer code;

    public VerificationCodeErrorException() {
        super("验证码不正确");
        this.code = CustomerCodeConstants.C800;
    }

    public VerificationCodeErrorException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public VerificationCodeErrorException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
    }
}
