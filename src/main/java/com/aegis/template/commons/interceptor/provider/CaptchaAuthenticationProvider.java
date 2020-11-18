package com.aegis.template.commons.interceptor.provider;

import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.exception.VerificationCodeErrorException;
import com.aegis.template.commons.filters.LoginFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 验证码 验证
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 10:22
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {

  @Override
  public boolean supports(Class<?> authentication) {
    return LoginFilter.CaptchaToken.class.isAssignableFrom(authentication);
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
    LoginFilter.CaptchaToken token = (LoginFilter.CaptchaToken) authentication;

    //todo 验证验证码是否正确
    //from cache[token.getKey()]
    if (!CustomerCodeConstants.C1234.toString().equalsIgnoreCase(token.getCaptcha())) {
      throw new VerificationCodeErrorException();
    }

    super.additionalAuthenticationChecks(userDetails, authentication);
  }
}
