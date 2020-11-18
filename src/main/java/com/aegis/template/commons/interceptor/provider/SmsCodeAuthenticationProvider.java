package com.aegis.template.commons.interceptor.provider;

import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.exception.VerificationCodeErrorException;
import com.aegis.template.commons.filters.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 邮箱验证码验证
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 10:58
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) {
    LoginFilter.SmsCodeAuthenticationToken authenticationToken = (LoginFilter.SmsCodeAuthenticationToken) authentication;

    String mobile = (String) authenticationToken.getPrincipal();
    String code = (String) authenticationToken.getCredentials();

    checkSmsCode(mobile, code);

    UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
    if (userDetails == null) {
      throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
    }
    // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
    LoginFilter.SmsCodeAuthenticationToken authenticationResult = new LoginFilter.SmsCodeAuthenticationToken(userDetails, authenticationToken.getCredentials(), userDetails.getAuthorities());

    authenticationResult.setDetails(authenticationToken.getDetails());

    return authenticationResult;
  }

  private void checkSmsCode(String mobile, String code) {

    //todo 验证验证码是否正确
    if(mobile.equalsIgnoreCase("1234")){
      log.info("验证验证码是否正确");
    }

    if (!CustomerCodeConstants.C1234.toString().equalsIgnoreCase(code)) {
      throw new VerificationCodeErrorException();
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return LoginFilter.SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
  }


  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
