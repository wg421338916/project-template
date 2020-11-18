package com.aegis.template.commons.constants;

/**
 * 常量
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 16:47
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class SecurityLoginConstants {
  /**
   * 验证码.
   */
  public static final String LOGIN_TYPE_CAPTCHA = "CAPTCHA";
  /**
   * 邮箱
   */
  public static final String LOGIN_TYPE_EMAIL = "EMAIL";
  /**
   * 短信登录提交
   */
  public static final String LOGIN_TYPE_LOG = "LOG";
  /**
   * 未知登录方式
   */
  public static final String LOGIN_TYPE_NONE = "NONE";
  /**
   * 短信登录提交
   */
  public static final String LOGIN_TYPE_SMS = "SMS";
  /**
   * 验证码VALUE
   */
  public static final String PARAMETER_CAPTCHA_VALUE = "captchaValue";
  /**
   * 登录名称
   */
  public static final String PARAMETER_LOGIN_NAME = "loginName";
  /**
   * 登录类型
   */
  public static final String PARAMETER_LOGIN_TYPE = "loginType";
  /**
   * 登录密钥
   */
  public static final String PARAMETER_SECRET = "loginSecret";
  /**
   * 登录密钥
   */
  public static final String PASSWORD = "password";
  /**
   * 记住密码参数
   */
  public static final String REMEMBER_ME = "rememberMe";
  /**
   * 登录名称
   */
  public static final String USER_NAME = "username";

  private SecurityLoginConstants() {
  }
}
