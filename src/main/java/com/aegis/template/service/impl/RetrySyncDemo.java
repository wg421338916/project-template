package com.aegis.template.service.impl;

import com.aegis.template.commons.utils.retry.DispatcherQueue;
import com.aegis.template.commons.utils.retry.RetryUtils;
import com.aegis.template.commons.utils.retry.RetryWrapperMessage;
import com.aegis.template.commons.utils.retry.annotation.RetrySyncListener;
import com.aegis.template.commons.utils.retry.annotation.RetrySyncPolicy;
import com.aegis.template.commons.utils.retry.annotation.RetrySyncQueue;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * RetrySyncDemo demo
 * 1。使用注解的话，必须开开启 @EnableRetrySync,一般在 Application 入口类上开启
 * 2。使用注解的话，类上必须有 @RetrySyncListener 注解
 * 3。异使用注解的话，步方法上必须包含 @RetrySyncQueue 注解，方法必须是运回Boolean类型及有一个参数
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 19:20
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Service
@RetrySyncListener
public class RetrySyncDemo {

  DispatcherQueue<Object> sayHello5;

  public RetrySyncDemo() throws IOException {
    sayHello5 = RetryUtils.getExponentialPersistenceQueue("sayHello5");
    sayHello5.register(this::sayHello5);
  }

  private boolean sayHello5(RetryWrapperMessage<Object> objectRetryWrapperMessage) {
    System.out.println(objectRetryWrapperMessage.getPayload() + "sayHello5");
    return true;
  }

  @RetrySyncQueue(name = "sayHello1")
  public boolean sayHello1(Object demo) {
    System.out.println(demo);
    return true;
  }

  @RetrySyncQueue(name = "sayHello2", delayMillis = 2000, isPersistent = false)
  public Boolean sayHello2(Object demo) {
    System.out.println(demo);
    return true;
  }

  @RetrySyncQueue(name = "sayHello3", policy = @RetrySyncPolicy(maxIntervalSec = 0, multiplier = 0, maxTryCount = 3))
  public boolean sayHello3(String demo) {
    System.out.println(demo);
    //注意,方法内部调用，不会走异步方法
    sayHello4(demo);

    //可以采用这样的方法替代如上方法
    sayHello5.put(demo);
    return true;
  }

  /**
   * 注意, 如果该方法被内部方法调用，则不走异步方法
   *
   * @param demo
   * @return
   */
  @RetrySyncQueue(name = "sayHello4", policy = @RetrySyncPolicy(maxIntervalSec = 0, multiplier = 0, maxTryCount = 3))
  private boolean sayHello4(String demo) {
    System.out.println(demo + "sayHello4"+"-内部类调用不走异步方法，和spring @sync 原理一样");
    return false;
  }
}
