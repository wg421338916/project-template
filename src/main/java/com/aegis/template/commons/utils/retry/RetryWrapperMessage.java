package com.aegis.template.commons.utils.retry;

import lombok.Getter;

/**
 * 尝试消息,T 对象最好重写toString方法，用来打印有用日志
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:29
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class RetryWrapperMessage<T> {
  @Getter
  private String id;
  @Getter
  private T payload;
  @Getter
  private Integer retryCount = 0;

  public RetryWrapperMessage(String id, Integer retryCount, T payload) {
    this.id = id;
    this.retryCount = retryCount;
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "RetryWrapperMessage{" +
        "id='" + id + '\'' +
        ", payload=" + payload +
        ", retryCount=" + retryCount +
        '}';
  }
}