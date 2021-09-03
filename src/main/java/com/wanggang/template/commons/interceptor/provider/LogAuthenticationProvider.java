package com.wanggang.template.commons.interceptor.provider;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import com.wanggang.template.commons.exception.VerificationCodeErrorException;
import com.wanggang.template.commons.filters.LoginFilter;
import com.wanggang.template.model.bo.UserDetailsBO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

/**
 * 日志查看验证
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 10:58
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class LogAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) {
        LoginFilter.LogAuthenticationToken authenticationToken = (LoginFilter.LogAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        if (!"admin".equalsIgnoreCase(mobile)) {
            throw new VerificationCodeErrorException();
        }

        if (!CustomerCodeConstants.C1234.toString().equalsIgnoreCase(code)) {
            throw new VerificationCodeErrorException();
        }
        UserDetailsBO userDetails = new UserDetailsBO(mobile, code, Lists.newArrayList());

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        LoginFilter.SmsCodeAuthenticationToken authenticationResult = new LoginFilter.SmsCodeAuthenticationToken(userDetails, authenticationToken.getCredentials(), userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginFilter.LogAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
