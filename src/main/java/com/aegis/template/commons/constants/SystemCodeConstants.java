package com.aegis.template.commons.constants;

/**
 * 系统默认返回码
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class SystemCodeConstants {
  /**
   * 0 代表默认成功输出
   */
  public static final Integer C0 = 0;
  /**
   * 表示成功
   */
  public static final Integer C200 = 200;
  /**
   * 参数异常
   */
  public static final Integer C400 = 400;
  /**
   * 需要认证后访问
   */
  public static final Integer C401 = 401;
  /**
   * 系统拒绝访问
   */
  public static final Integer C403 = 403;
  /**
   * 表示资源不存在
   */
  public static final Integer C404 = 404;
  /**
   * 系统异常
   */
  public static final Integer C500 = 500;
  /**
   * 执行请求时
   */
  public static final Integer C504 = 504;

  protected SystemCodeConstants() {
  }
}
