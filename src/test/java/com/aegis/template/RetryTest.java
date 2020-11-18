package com.aegis.template;

import cn.hutool.core.date.DateUtil;
import com.aegis.template.commons.utils.retry.*;
import com.aegis.template.model.entity.User;
import com.aegis.template.model.vo.UserVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Stopwatch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
public class RetryTest {
  @Data
  public static class DemoA implements Serializable {
    private String type;
  }

  @Data
  public static class DemoB extends DemoA {

    private LocalDateTime dataB;

    public DemoB() {
    }

    public DemoB(LocalDateTime a) {
      this.dataB = a;
    }
  }

  @Data
  public static class DemoC extends DemoA {
    private LocalDateTime dataC;

    public DemoC() {
    }

    public DemoC(LocalDateTime a) {
      this.dataC = a;
    }
  }

  @Test
  public void batchGetExponentialPersistenceQueue2() throws IOException, InterruptedException {
    DispatcherQueue<UserVO> myUser = RetryUtils.getExponentialPersistenceQueue("testsss5");
    myUser.register(wt -> {
      UserVO t = wt.getPayload();
      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      Assert.assertTrue(t.getRealName().equalsIgnoreCase("ssssssss"));
      return true;
    });

    UserVO user = new UserVO();
    user.setRealName("sssssssss");
    user.setCreateTime(LocalDateTime.now());
    Boolean put = myUser.put(user, TimeUnit.MINUTES.toMillis(1L), false);
    Assert.assertTrue(put);

    UserVO user2 = new UserVO();
    user2.setRealName("sssssssss");
    user2.setCreateTime(LocalDateTime.now());
    Boolean put2 = myUser.put(user2, TimeUnit.MINUTES.toMillis(1L), false);
    Assert.assertFalse(put2);

    TimeUnit.MINUTES.sleep(2);
  }

  @Test
  public void batchGetExponentialPersistenceQueue() throws IOException, InterruptedException {
    AtomicInteger it = new AtomicInteger();
    AtomicInteger it2 = new AtomicInteger();

    Stopwatch stopwatch = Stopwatch.createStarted();

    DispatcherQueue<UserVO> myUser = RetryUtils.getExponentialPersistenceQueue("testsss5");
    myUser.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      int i = it.incrementAndGet();
      Assert.assertTrue(t.getRealName().equalsIgnoreCase("ssssssss"));
      return true;
    });

    IntStream.range(0, 10).forEach(i -> {
      new Thread(() -> {
        IntStream.range(0, 3000).forEach(i2 -> {
          UserVO user = new UserVO();
          user.setRealName("ssssssss");
          user.setCreateTime(LocalDateTime.now());
          myUser.put(user);

          it2.incrementAndGet();
        });
      }).start();

    });

    while (it.get() != 30000) {
      TimeUnit.MILLISECONDS.sleep(100);
    }

    long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
    System.out.println(elapsed);//80s - 12,500/s

    System.out.println(it.get());
    System.out.println(it2.get());
    Assert.assertTrue(it.get() == it2.get());

    TimeUnit.MINUTES.sleep(2);
  }

  @Test
  public void getExponentialMemoryQueueTest() throws InterruptedException {
    DispatcherQueue<UserVO> myUser = RetryUtils.getExponentialMemoryQueue("abc");
    myUser.register(t -> {
      //todo sth
      System.out.println("111111111111");
      return false;
    });

    UserVO user = new UserVO();
    user.setCreateTime(LocalDateTime.now());
    myUser.put(user);

    TimeUnit.MINUTES.sleep(2);

    Assert.assertNotNull(myUser);

  }

  @Test
  public void getExponentialPersistenceQueueTestBatch() throws IOException, InterruptedException {
    DispatcherQueue<UserVO> myUser1 = RetryUtils.getExponentialPersistenceQueue("tttttt1");
    DispatcherQueue<String> myUser2 = RetryUtils.getExponentialPersistenceQueue("tttttt2");
    DispatcherQueue<Integer> myUser3 = RetryUtils.getExponentialPersistenceQueue("tttttt3");
    DispatcherQueue<Double> myUser4 = RetryUtils.getExponentialPersistenceQueue("tttttt4");
    DispatcherQueue<Float> myUser5 = RetryUtils.getExponentialPersistenceQueue("tttttt5");
    DispatcherQueue<BigDecimal> myUser6 = RetryUtils.getExponentialPersistenceQueue("tttttt6");
    DispatcherQueue<Short> myUser7 = RetryUtils.getExponentialPersistenceQueue("tttttt7");

    myUser1.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      return true;
    });
    myUser2.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });
    myUser3.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });
    myUser4.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });
    myUser5.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });
    myUser6.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });
    myUser7.register(t -> {
      log.error("----------------------------------" + t + "--" + LocalDateTime.now());
      return true;
    });


    UserVO user = new UserVO();
    user.setRealName("ssssssss");
    user.setCreateTime(LocalDateTime.now());
    myUser1.put(user);
    myUser2.put("test");
    myUser3.put(1);
    myUser4.put(2D);
    myUser5.put(5F);
    myUser6.put(new BigDecimal(100));
    myUser7.put((short) 100);

    TimeUnit.MINUTES.sleep(2);

    Assert.assertNotNull(myUser1);

  }

  @Test
  public void getFixedMemoryQueueTest() throws InterruptedException {
    DispatcherQueue<User> myUser = RetryUtils.getFixedMemoryQueue("aaaa");
    myUser.register(t -> {
      System.out.println(DateUtil.now() + "---" + t);
      return false;
    });


    User user = new User();
    user.setVersion(1);
    myUser.put(user);

    TimeUnit.MINUTES.sleep(2);

    Assert.assertNotNull(myUser);

  }

  @Test
  public void getFixedPersistenceQueueTest() throws IOException, InterruptedException {
    DispatcherQueue<UserVO> myUser = RetryUtils.getFixedPersistenceQueue("test2");
    myUser.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());

      return false;
    });


    UserVO user = new UserVO();
    user.setRealName("ssssssss");
    user.setCreateTime(LocalDateTime.now());
    myUser.put(user);

    TimeUnit.SECONDS.sleep(85);

    Assert.assertNotNull(myUser);
  }

  @Test
  public void getMemoryQueueTest() throws IOException, InterruptedException {
    DispatcherQueue<UserVO> myUser = RetryUtils.getMemoryQueue("asd", new ExponentialRetryPolicy(3, 1000, 90000000, 2));
    myUser.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      t.setRealName("s" + LocalDateTime.now());
      return false;
    });


    UserVO user = new UserVO();
    user.setRealName("ssssssss");
    user.setCreateTime(LocalDateTime.now());
    myUser.put(user);

    TimeUnit.SECONDS.sleep(10);
    Assert.assertNotNull(myUser);

  }

  @Test
  public void getPersistenceQueueBatchTest() throws IOException, InterruptedException {

    AtomicInteger it = new AtomicInteger();
    DispatcherQueue<UserVO> myUser = RetryUtils.getExponentialPersistenceQueue("test6");
    myUser.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      int andDecrement = it.incrementAndGet();
      if (andDecrement % 3 == 0)
        return true;

      return false;
    });


    UserVO user = new UserVO();
    user.setRealName("ssssssss");
    user.setCreateTime(LocalDateTime.now());
    myUser.put(user);

    TimeUnit.MINUTES.sleep(2);

    Assert.assertNotNull(myUser);

  }

  @Test
  public void getPersistenceQueueSuccessTest() throws IOException, InterruptedException {
    AtomicInteger it = new AtomicInteger();
    DispatcherQueue<UserVO> myUser = RetryUtils.getPersistenceQueue("test5", new ExponentialRetryPolicy(3, 1000, 90000000, 2));
    myUser.register(wt -> {
      UserVO t = wt.getPayload();

      System.out.println(t.getRealName() + "-" + t.getCreateTime());
      log.error("----------------------------------" + LocalDateTime.now());
      int i = it.incrementAndGet();

      if (i == 3)
        return true;

      return false;
    });


    UserVO user = new UserVO();
    user.setRealName("ssssssss");
    user.setCreateTime(LocalDateTime.now());
    myUser.put(user);

    TimeUnit.MINUTES.sleep(1);

    Assert.assertNotNull(myUser);

  }

  @Test
  public void getPersistenceQueueTest() throws IOException, InterruptedException {
    DispatcherQueue<DemoA> myUser = RetryUtils.getPersistenceQueue("test5XX", new ExponentialRetryPolicy(3, 10000, 90000000, 2));
    myUser.register(wt -> {
      DemoA t = wt.getPayload();

      if (t.getType().equalsIgnoreCase("a")) {
        System.out.println("aaaaaaaaaaa");
      } else if (t.getType().equalsIgnoreCase("b")) {
        System.out.println("bbbbbbbbbb");
      } else if (t.getType().equalsIgnoreCase("c")) {
        System.out.println("ccccccccc");
      }
      return false;
    });

    DemoA da = new DemoA();
    da.setType("a");
    myUser.put(da);

    DemoA db = new DemoB(LocalDateTime.now());
    db.setType("b");
    myUser.put(db);

    DemoA dc = new DemoC(LocalDateTime.now());
    dc.setType("c");
    myUser.put(dc);

    TimeUnit.MINUTES.sleep(1);

    Assert.assertNotNull(myUser);

  }


  @Test
  public void getPersistenceQueueTestObj() throws IOException, InterruptedException {
    DispatcherQueue<Object> myUser = RetryUtils.getExponentialPersistenceQueue("test5XX");
    myUser.register(wt -> {
      Object t = wt.getPayload();

      System.out.println(t.getClass());
      return true;
    });

    DemoA da = new DemoA();
    da.setType("a");
    myUser.put(da);

    DemoA db = new DemoB(LocalDateTime.now());
    db.setType("b");
    myUser.put(db);

    DemoA dc = new DemoC(LocalDateTime.now());
    dc.setType("c");
    myUser.put(dc);

    TimeUnit.MINUTES.sleep(1);

    Assert.assertNotNull(myUser);

  }

  @Test(expected = IllegalArgumentException.class)
  public void getPersistenceQueueTest2() throws IOException {
    DispatcherQueue<UserVO> myUser = RetryUtils.getPersistenceQueue("test5", new ExponentialRetryPolicy(3, 1000, 90000000, 2));
    DispatcherQueue<String> myUser2 = RetryUtils.getPersistenceQueue("test5", new ExponentialRetryPolicy(3, 1000, 90000000, 2));
    Assert.assertNotNull(myUser);
  }

  @Test(expected = SerializationException.class)
  public void jsonTest(){
    UserTest u = new UserTest();
    u.setName("demo");
    u.setNow(LocalDateTime.now());
    u.setNow2(new Date());

    GenericJackson2JsonRetrySerializer serializer = new GenericJackson2JsonRetrySerializer();
    byte[] serialize = serializer.serialize(u);

    System.out.println(new String(serialize));

    byte[] bytes = "[\"com.aegis.template.RetryTest$UserTest\",{\"name1\":null,\"now\":[2020,11,12,14,42,36,664000000],\"now2\":[\"java.util.Date\",1605163357055],\"name\":\"demo\"}]".getBytes();
    UserTest deserialize = serializer.deserialize(bytes, new TypeReference<UserTest>() {
    });

    byte[] bytes2 = "[\"com.aegis.template.RetryTest$UserTest\",{\"name\":\"demo\"}]".getBytes();
    UserVO deserialize2 = serializer.deserialize(bytes2, new TypeReference<UserVO>() {
    });
  }

  @Data
  public static class UserTest{
    public UserTest(){}
    private String name1;
    private LocalDateTime now;
    private Date now2;
    private String name;
  }
}
