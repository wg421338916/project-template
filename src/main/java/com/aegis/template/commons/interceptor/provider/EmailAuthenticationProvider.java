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
public class EmailAuthenticationProvider implements AuthenticationProvider {
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) {
    LoginFilter.EmailAuthenticationToken authenticationToken = (LoginFilter.EmailAuthenticationToken) authentication;

    String email = (String) authenticationToken.getPrincipal();
    String code = (String) authenticationToken.getCredentials();

    checkCode(email, code);


    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    if (userDetails == null) {
      throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
    }

    // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
    LoginFilter.EmailAuthenticationToken authenticationResult = new LoginFilter.EmailAuthenticationToken(userDetails, authenticationToken.getCredentials(), userDetails.getAuthorities());

    authenticationResult.setDetails(authentication.getDetails());

    return authenticationResult;
  }

  private void checkCode(String email, String code) {

    //todo 验证验证码是否正确
    //var value = cache[email]
    if (email.equalsIgnoreCase("1234")) {
      log.info("验证码不正确");
    }
    if (!CustomerCodeConstants.C1234.toString().equalsIgnoreCase(code)) {
      throw new VerificationCodeErrorException();
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return LoginFilter.EmailAuthenticationToken.class.isAssignableFrom(authentication);
  }

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}

