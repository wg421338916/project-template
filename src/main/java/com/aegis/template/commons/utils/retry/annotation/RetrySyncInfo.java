package com.aegis.template.commons.utils.retry.annotation;

import com.aegis.template.commons.utils.retry.DispatcherQueue;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 异步重试队列注解
 * 1。必须开开启 @EnableRetrySync
 * 2。类上必须有 @RetrySyncListener 注解
 * 3。异步方法上必须包含 @RetrySyncQueue 注解，方法必须是运回Boolean类型及有一个参数
 * <p>
 *
 * @author 王刚
 * @version 1.0
 * @date 2019/12/27
 * @since 1.0.0
 */
@Data
class RetrySyncInfo {
  private Object bean;
  private Method method;
  private DispatcherQueue<Object> queue;
}
