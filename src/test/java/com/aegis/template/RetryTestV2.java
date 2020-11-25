package com.aegis.template;

import com.aegis.template.service.impl.RetrySyncDemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * retry demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 14:29
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("dev")
@EnableAsync
public class RetryTestV2 {

  @Autowired
  RetrySyncDemo retrySyncTest;

  @Test
  public void annotationTest() {
    retrySyncTest.sayHello1("aaaaaaa");
    retrySyncTest.sayHello1(LocalDateTime.now());

  }

  @Test
  public void annotationTest2() throws InterruptedException {
    IntStream.range(1, 20).forEach(t -> {
      new Thread(() -> {
        System.out.println("dddddddddddddd"+t);
        retrySyncTest.sayHello2("aaaaaaaaaaa"+t);
      }).start();
    });

    TimeUnit.SECONDS.sleep(100);
  }

  @Test
  public void annotationTest22() throws InterruptedException {
    retrySyncTest.sayHello2("aaaaaaaaaaa");

    TimeUnit.SECONDS.sleep(100);
  }


  @Test
  public void annotationTest3() throws InterruptedException {
    retrySyncTest.sayHello3("aaaaaaa");

    TimeUnit.SECONDS.sleep(100);
  }
}