package com.aegis.template.commons.utils.retry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步重试队列注解
 * 1。必须开开启 @EnableRetrySync
 * 2。类上必须有 @RetrySyncListener 注解
 * 3。异步方法上必须包含 @RetrySyncQueue 注解，方法必须是运回Boolean类型及有一个参数(使用持久化队列参数必须支持序列化，反序列化)
 * <p>
 *
 * @author 王刚
 * @version 1.0
 * @date 2019/12/27
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetrySyncQueue {
  /**
   * 队列名称，队列名称全局唯一
   *
   * @return 队列名称
   */
  String name();

  /**
   * 延迟执行时间
   *
   * @return 加入队列延迟执行时间
   */
  long delayMillis() default 0L;

  /**
   * 是否持久化,默认为执行化
   *
   * @return true 为执行化 ，false 不为执行化
   */
  boolean isPersistent() default true;

  /**
   * 队列中的数据是否可以重复,如果相同数据已经被消费，则可继续另入
   *
   * @return 是否重复
   */
  boolean canRepeat() default true;

  /**
   * 策略默认为指数重试
   *
   * @return
   */
  RetrySyncPolicy policy() default @RetrySyncPolicy();
}
