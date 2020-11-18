package com.aegis.template.commons.filters;

import cn.hutool.core.util.StrUtil;
import com.aegis.template.commons.constants.SecurityLoginConstants;
import com.aegis.template.commons.exception.NotSupportedLoginTypeException;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Objects;

/**
 * 预登录验证
 * ---------------方式1--------
 * captchaValue:1234
 * loginType:CAPTCHA
 * loginName:admin
 * loginSecret:1234
 * ----------------------------
 * @author wg
 * @version 1.0
 * @date 2020/3/28 13:43
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
  public LoginFilter(String processesUrl) {
    super(new AntPathRequestMatcher(processesUrl, "POST"));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    if (!HttpMethod.POST.name().equals(request.getMethod())) {
      throw new NotSupportedLoginTypeException();
    }

    String loginType = obtainLoginType(request);
    if (StrUtil.isBlank(loginType)) {
      throw new NotSupportedLoginTypeException();
    }

    AbstractAuthenticationToken token;

    String loginName = obtainLoginName(request);
    String secret = obtainSecret(request);

    switch (loginType.toUpperCase()) {
      case SecurityLoginConstants.LOGIN_TYPE_CAPTCHA:
        String key = obtainCaptchaKey(request);
        String captcha = obtainCaptchaValue(request);
        token = new CaptchaToken(loginName, secret, key, captcha);
        break;
      case SecurityLoginConstants.LOGIN_TYPE_EMAIL:
        if (StrUtil.isBlank(secret)) {
          secret = obtainCaptchaValue(request);
        }
        token = new EmailAuthenticationToken(loginName, secret);
        break;
      case SecurityLoginConstants.LOGIN_TYPE_SMS:
        if (StrUtil.isBlank(secret)) {
          secret = obtainCaptchaValue(request);
        }
        token = new SmsCodeAuthenticationToken(loginName, secret);
        break;
      case SecurityLoginConstants.LOGIN_TYPE_LOG:
        token = new LogAuthenticationToken(loginName, secret);
        break;
      default:
        throw new NotSupportedLoginTypeException();
    }

    this.setDetails(request, token);
    return this.getAuthenticationManager().authenticate(token);
  }

  @Nullable
  protected String obtainLoginType(HttpServletRequest request) {
    return request.getParameter(SecurityLoginConstants.PARAMETER_LOGIN_TYPE);
  }

  @Nullable
  protected String obtainLoginName(HttpServletRequest request) {
    return request.getParameter(SecurityLoginConstants.PARAMETER_LOGIN_NAME);
  }

  @Nullable
  protected String obtainSecret(HttpServletRequest request) {
    return request.getParameter(SecurityLoginConstants.PARAMETER_SECRET);
  }

  @Nullable
  private String obtainCaptchaKey(HttpServletRequest request) {
    return request.getSession().getId();
  }

  @Nullable
  private String obtainCaptchaValue(HttpServletRequest request) {
    return request.getParameter(SecurityLoginConstants.PARAMETER_CAPTCHA_VALUE);
  }

  protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }

  /**
   * 邮箱登录验证
   * principal：邮箱
   * credentials：验证码
   */
  public static class EmailAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public EmailAuthenticationToken(Object principal, Object credentials) {
      super(principal, credentials);
    }

    public EmailAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
      super(principal, credentials, authorities);

    }
  }

  /**
   * 短信登录验证
   * principal：手机号码
   * credentials：验证码
   */
  public static class SmsCodeAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public SmsCodeAuthenticationToken(Object principal, Object credentials) {
      super(principal, credentials);
    }

    public SmsCodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
      super(principal, credentials, authorities);

    }
  }

  /**
   * 短信登录验证
   * principal：手机号码
   * credentials：验证码
   */
  public static class LogAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public LogAuthenticationToken(Object principal, Object credentials) {
      super(principal, credentials);
    }
  }

  /**
   * 验证码登陆验证token
   */
  public class CaptchaToken extends UsernamePasswordAuthenticationToken {
    private String captcha;
    private String key;

    public CaptchaToken(Object principal, Object credentials, String key, String captcha) {
      super(principal, credentials);
      this.captcha = captcha;
      this.key = key;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof CaptchaToken)) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      CaptchaToken that = (CaptchaToken) o;
      return Objects.equals(captcha, that.captcha) &&
          Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), captcha, key);
    }

    public String getCaptcha() {
      return this.captcha;
    }

    public void setCaptcha(String verificationCode) {
      this.captcha = verificationCode;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }
  }
}
