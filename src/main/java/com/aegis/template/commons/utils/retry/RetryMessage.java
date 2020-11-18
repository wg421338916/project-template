package com.aegis.template.commons.utils.retry;

import cn.hutool.core.lang.UUID;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 尝试消息,T 对象最好重写toString方法，用来打印有用日志
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:29
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Data
class RetryMessage<T> implements Delayed, Serializable {
  private static final long serialVersionUID = 3819111152108413236L;
  private long delayTime = 0;
  private String id;
  private T payload;
  private String queueName;
  private Integer retryCount = 0;

  public RetryMessage(T payload, String queueName) {
    this();
    this.payload = payload;
    this.queueName = queueName;
  }

  public RetryMessage() {
    this.id = UUID.fastUUID().toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public int compareTo(@NotNull Delayed o) {
    RetryMessage<T> message = (RetryMessage<T>) o;
    if (message.getId().equals(this.id)) {
      return 0;
    }

    long diff = this.delayTime - message.delayTime;
    if (diff <= 0) {
      return -1;
    }

    return 1;
  }

  @Override
  public long getDelay(@NotNull TimeUnit unit) {
    return delayTime - System.currentTimeMillis();
  }

  @Override
  public int hashCode() {
    return this.payload.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    RetryMessage<T> t = (RetryMessage<T>) obj;
    return payload.equals(t.getPayload());
  }

  @Override
  public String toString() {
    return "RetryMessage{" +
        "id='" + id + '\'' +
        ", payload=" + payload.toString() +
        ", retryCount=" + retryCount +
        '}';
  }
}