package com.aegis.template.commons.utils.retry.annotation;

/**
 * 异步重试队列注解 策略
 * <p>
 *
 * @author 王刚
 * @version 1.0
 * @date 2019/12/27
 * @since 1.0.0
 */
public @interface RetrySyncPolicy {
  /**
   * 最大重试次数，默认为20次
   *
   * @return 最大重试次数
   */
  int maxTryCount() default 20;

  /**
   * 重试间隔基数时间，默认为4s
   * 如果是固定间隔重试：则每4s重拾一次，直到达到 20次（maxTryCount） 为止
   * 如果是指数间隔重试：则根据 multiplier 的值，一直增加，最大间隔时间 <= maxIntervalSec，最大重试次数不超过maxTryCount
   *
   * @return
   */
  long initialIntervalSec() default 4;

  /**
   * 最大重试时间间隔 7天
   * 如果<=0 使用固定重拾策略，否则使用指数失败重试策略
   *
   * @return
   */
  long maxIntervalSec() default 604800;

  /***
   * 指数重试基数，如果<=1 使用固定重拾策略，否则使用指数失败重试策略
   * @return
   */
  double multiplier() default 1.8;
}


