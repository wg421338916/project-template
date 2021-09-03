package com.wanggang.template.commons.exception;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 不支持的登录类型
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 16:55
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class NotSupportedLoginTypeException extends AuthenticationException implements ResponseException {
    @Getter
    protected final Integer code;

    public NotSupportedLoginTypeException() {
        super("不支持的登录类型");
        this.code = CustomerCodeConstants.C801;
    }

    public NotSupportedLoginTypeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public NotSupportedLoginTypeException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
    }
}
