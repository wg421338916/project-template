package com.aegis.template;

import com.aegis.template.commons.utils.cache.CacheLock;
import com.aegis.template.commons.utils.cache.ehcache.EhCacheUtils;
import com.aegis.template.model.vo.UserVO;
import com.aegis.template.service.impl.CacheServiceDemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * EhCacheUtilsTest
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 18:17
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("dev")
public class EhCacheUtilsAndCacheManagerTest {

  @Autowired
  private CacheServiceDemo redisCacheServiceDemo;

  @Test
  public void batchTest() {
    for (int i = 1; i <= 50000; i++) {
      EhCacheUtils.put("test" + i, i);

      EhCacheUtils.get("test1");
    }

    Integer o2 = EhCacheUtils.get("test" + 50000, Integer.class);
    Integer o1 = EhCacheUtils.get("test" + 2, Integer.class);
    Integer o3 = EhCacheUtils.get("test" + 1, Integer.class);

    List<String> strings = EhCacheUtils.listKeys();
    Assert.assertTrue(strings.size() == 5000);

    Assert.assertNull(o1);
    Assert.assertNotNull(o2);
    Assert.assertNotNull(o3);
  }

  @Test
  public void cacheServiceEhCacheTest() {
    UserVO userDetailsByUid1 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("1");
    UserVO userDetailsByUid3 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("1");
    Assert.assertTrue(userDetailsByUid1.getRealName().equalsIgnoreCase(userDetailsByUid3.getRealName()));
    redisCacheServiceDemo.updateUserInfoInEhCache(userDetailsByUid3);
    UserVO userDetailsByUid4 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("1");
    Assert.assertTrue(userDetailsByUid4.getRealName().equalsIgnoreCase("test2"));

    UserVO userDetailsByUid11 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("2");
    UserVO userDetailsByUid31 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("2");
    Assert.assertTrue(userDetailsByUid11.getRealName().equalsIgnoreCase(userDetailsByUid31.getRealName()));
    redisCacheServiceDemo.updateUserInfoInEhCache(userDetailsByUid31);
    UserVO userDetailsByUid41 = redisCacheServiceDemo.getUserDetailsByUidInEhCache("2");
    Assert.assertTrue(userDetailsByUid41.getRealName().equalsIgnoreCase("test2"));

    Object o = EhCacheUtils.get("1");
    Object o2 = EhCacheUtils.get("1");

    redisCacheServiceDemo.delUserInfoByIdInEhCache("1");
    redisCacheServiceDemo.delUserInfoByIdInEhCache("2");
    Object o3 = EhCacheUtils.get("1");
    Assert.assertNotNull(o);
    Assert.assertNotNull(o2);
    Assert.assertNull(o3);
  }

  @Test
  public void cacheServiceRedisTest() {
    UserVO userDetailsByUid1 = redisCacheServiceDemo.getUserDetailsByUidInRedis("1");
    UserVO userDetailsByUid3 = redisCacheServiceDemo.getUserDetailsByUidInRedis("1");
    Assert.assertTrue(userDetailsByUid1.getRealName().equalsIgnoreCase(userDetailsByUid3.getRealName()));
    redisCacheServiceDemo.updateUserInfoInRedis(userDetailsByUid3);
    UserVO userDetailsByUid4 = redisCacheServiceDemo.getUserDetailsByUidInRedis("1");
    Assert.assertTrue(userDetailsByUid4.getRealName().equalsIgnoreCase("test2"));

    UserVO userDetailsByUid11 = redisCacheServiceDemo.getUserDetailsByUidInRedis("2");
    UserVO userDetailsByUid31 = redisCacheServiceDemo.getUserDetailsByUidInRedis("2");
    Assert.assertTrue(userDetailsByUid11.getRealName().equalsIgnoreCase(userDetailsByUid31.getRealName()));
    redisCacheServiceDemo.updateUserInfoInRedis(userDetailsByUid31);
    UserVO userDetailsByUid41 = redisCacheServiceDemo.getUserDetailsByUidInRedis("2");
    Assert.assertTrue(userDetailsByUid41.getRealName().equalsIgnoreCase("test2"));

    redisCacheServiceDemo.delUserInfoByIdInRedis("1");
    redisCacheServiceDemo.delUserInfoByIdInRedis("2");
  }

  /**
   * 持久化测试
   */
  @Test
  public void demoTest() {
//    IntStream.range(0, 200).forEach(i -> {
//      EhCacheUtils.putIfAbsent("demo", "demo" + i, i);
//    });

    Object o1 = EhCacheUtils.get("demo", "demo" + 0);
    Object o2 = EhCacheUtils.get("demo", "demo" + 150);

    EhCacheUtils.flush("demo");

    Assert.assertNotNull(o1);
    Assert.assertNotNull(o2);
  }

  @Test
  public void lock() throws InterruptedException {

    try (CacheLock demo = EhCacheUtils.getLock("demo")) {
      boolean b = demo.tryLock(5, TimeUnit.SECONDS);
      System.out.println(b);
      Assert.assertTrue(b);
    }

    new Thread(() -> {
      CacheLock demo = EhCacheUtils.getLock("demo");
      boolean b = demo.tryLock(5, TimeUnit.SECONDS);
      System.out.println(b);
      Assert.assertTrue(b);

      boolean b2 = demo.tryLock(5, TimeUnit.SECONDS);
      System.out.println(b2);
      Assert.assertTrue(b2);
    }).start();

    TimeUnit.SECONDS.sleep(1);
    CacheLock demo = EhCacheUtils.getLock("demo");
    boolean b = demo.tryLock(0, TimeUnit.SECONDS);
    System.out.println(b);
    Assert.assertFalse(b);

    demo.unlock();
    boolean b2 = demo.tryLock(0, TimeUnit.SECONDS);
    System.out.println(b2);
    Assert.assertFalse(b2);

    CacheLock demo2 = EhCacheUtils.getLock("refuseRepeatSubmitLock", "demo");
    boolean abc = demo2.tryLock(3, TimeUnit.SECONDS);
    System.out.println(abc);
    Assert.assertTrue(abc);

    TimeUnit.SECONDS.sleep(1);

    new Thread(() -> {
      CacheLock demo22 = EhCacheUtils.getLock("refuseRepeatSubmitLock", "demo");
      boolean b22 = demo22.tryLock();
      System.out.println(b22);
      Assert.assertFalse(b22);
    }).start();

    TimeUnit.SECONDS.sleep(3);

    new Thread(() -> {
      CacheLock demo22 = EhCacheUtils.getLock("refuseRepeatSubmitLock", "demo");
      boolean b22 = demo22.tryLock(5, TimeUnit.SECONDS);
      System.out.println(b22);
      Assert.assertTrue(b22);
    }).start();
    TimeUnit.SECONDS.sleep(1);

  }

  @Test
  public void lock2() throws InterruptedException {
    CacheLock demo2 = EhCacheUtils.getLock("refuseRepeatSubmitLock", "demo");
    boolean abc = demo2.tryLock(3, TimeUnit.SECONDS);
    System.out.println(abc);
    Assert.assertTrue(abc);

    TimeUnit.SECONDS.sleep(4);
    demo2.unlock();
  }

  @Test
  public void moreThreadTest() throws InterruptedException {
    IntStream.rangeClosed(0, 100).forEach(t -> {
      new Thread(() -> {
        EhCacheUtils.put("demo" + t, t);

        Boolean aBoolean = EhCacheUtils.putIfAbsent("demo" + t, t);
        System.out.println(aBoolean);
      }).start();
    });

    IntStream.rangeClosed(0, 100).forEach(t -> {
      new Thread(() -> {
        EhCacheUtils.get("demo" + t);
      }).start();
    });

    TimeUnit.SECONDS.sleep(5);

    Assert.assertNotNull(EhCacheUtils.get("demo1"));
  }

  @Test
  public void test() {
    EhCacheUtils.put("test", "test");

    Object o = EhCacheUtils.get("test");

    Assert.assertTrue(o.toString().equalsIgnoreCase("test"));

    EhCacheUtils.put("test", 1);
    Object o2 = EhCacheUtils.get("test");
    Assert.assertTrue(o2 instanceof Integer);
    Assert.assertTrue((Integer) o2 == 1);

    EhCacheUtils.remove("test");
    Object o3 = EhCacheUtils.get("test");
    Assert.assertNull(o3);

    EhCacheUtils.put("test2", "test");
    EhCacheUtils.put("test3", "test");
    EhCacheUtils.removeAll("sysCache");
    Object test2 = EhCacheUtils.get("test2");
    Object test3 = EhCacheUtils.get("test3");
    Assert.assertNull(test2);
    Assert.assertNull(test3);


    EhCacheUtils.putIfAbsent("ok", "ok1");
    EhCacheUtils.putIfAbsent("ok", "ok2");
    String ok = EhCacheUtils.get("ok", String.class);
    Assert.assertTrue(ok.equalsIgnoreCase("ok1"));
  }

  @Test
  public void testObject() {
    UserVO userVO = new UserVO();
    userVO.setRealName("test");

    List<Object> objects = new ArrayList<>();
    objects.add(userVO);
    objects.add(1);
    objects.add("string");

    EhCacheUtils.put("test", objects);

    List<Object> o = (List<Object>) EhCacheUtils.get("test");

    Assert.assertTrue(o.get(1) instanceof Integer);
  }

  @Test
  public void userString() {
    UserVO userVO = new UserVO();
    userVO.setRealName("test");

    EhCacheUtils.put("test", userVO);

    UserVO o = (UserVO) EhCacheUtils.get("test");

    Assert.assertTrue(o.getRealName().equalsIgnoreCase("test"));
  }
}
