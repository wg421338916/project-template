package com.aegis.template;

import com.aegis.template.commons.constants.RedisKeyConstants;
import com.aegis.template.commons.utils.cache.CacheLock;
import com.aegis.template.commons.utils.cache.redis.RedisUtils;
import com.aegis.template.model.entity.User;
import com.aegis.template.model.vo.UserVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * RedisUtilsTest
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/14 18:37
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("dev")
public class RedisUtilsTest {


  /**
   * 递减
   */
  @Test
  public void decr() {
    RedisUtils.del(RedisKeyConstants.DEMO1);
    RedisUtils.del(RedisKeyConstants.DEMO2);

    long decr = RedisUtils.decr(RedisKeyConstants.DEMO1, 1);
    Assert.assertTrue(-1L == decr);

    decr = RedisUtils.decr(RedisKeyConstants.DEMO1, 1);
    Assert.assertTrue(-2L == decr);

    RedisUtils.del(RedisKeyConstants.DEMO1);
  }


  @Test
  public void del() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);

    Object o1 = RedisUtils.get(RedisKeyConstants.DEMO1);
    Object o2 = RedisUtils.get(RedisKeyConstants.DEMO2);

    Assert.assertTrue(o1 == null);
    Assert.assertTrue(o2 == null);
  }

  @Test
  public void expire() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    long decr = RedisUtils.decr(RedisKeyConstants.DEMO1, 1);
    RedisUtils.expire(RedisKeyConstants.DEMO1, 1);
    long expire = RedisUtils.getExpire(RedisKeyConstants.DEMO1);
//    Assert.assertTrue(expire == 1);
    TimeUnit.SECONDS.sleep(2);
    Object o = RedisUtils.get(RedisKeyConstants.DEMO1);
    Assert.assertTrue(o == null);
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void get() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.set(RedisKeyConstants.DEMO1, new UserVO(LocalDateTime.now(),"test","1"));
    UserVO o = (UserVO)RedisUtils.get(RedisKeyConstants.DEMO1);
    Assert.assertTrue(o.getRealName().equalsIgnoreCase("test"));
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void getExpire() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.decr(RedisKeyConstants.DEMO1, 1);
    RedisUtils.expire(RedisKeyConstants.DEMO1, 10);
    long expire = RedisUtils.getExpire(RedisKeyConstants.DEMO1);

    Assert.assertTrue(expire <= 10 && expire > 5);
  }


  @Test
  public void hDecr() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, String item, void by
    double test = RedisUtils.hDecr(RedisKeyConstants.DEMO1, "test", 1.1D);
    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test");
    Assert.assertTrue(test == ((BigDecimal) test1).doubleValue());

    test = RedisUtils.hDecr(RedisKeyConstants.DEMO1, "test", 1);

    Assert.assertTrue((test + 1) == ((BigDecimal) test1).doubleValue());

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  @Test
  public void hDel() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void... item
    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test1", 1);
    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test2", 1);

    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    Object test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue((Integer) test1 == 1 && (Integer) test2 == 1);

    RedisUtils.hDel(RedisKeyConstants.DEMO1, "test1");

    test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue(test1 == null && (Integer) test2 == 1);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  @Test
  public void hGet() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test1", 1);
    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test2", 2);

    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    Object test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue((Integer) test1 == 1 && (Integer) test2 == 2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);

    RedisUtils.hSet(RedisKeyConstants.DEMO1,"test",new UserVO(LocalDateTime.now(),"test","1"));
    UserVO a = (UserVO)RedisUtils.hGet(RedisKeyConstants.DEMO1, "test");
    Assert.assertTrue(a.getRealName().equalsIgnoreCase("test"));
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void hHasKey() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    boolean test11 = RedisUtils.hHasKey(RedisKeyConstants.DEMO1, "test1");
    Assert.assertFalse(test11);

    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test1", 1);
    test11 = RedisUtils.hHasKey(RedisKeyConstants.DEMO1, "test1");
    Assert.assertTrue(test11);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  @Test
  public void hIncr() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, String item, void by

    double test = RedisUtils.hIncr(RedisKeyConstants.DEMO1, "test", 1);
    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test");
    Assert.assertTrue(test == (Integer) test1);

    test = RedisUtils.hIncr(RedisKeyConstants.DEMO1, "test", 1);

    Assert.assertTrue((test - 1) == (Integer) test1);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  @Test
  public void hSet() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, String item, void value
    //    RedisKeyConstants key, String item, void value, void time

    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test1", 1, 1);
    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test2", 2);

    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    Object test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue((Integer) test1 == 1 && (Integer) test2 == 2);

    TimeUnit.SECONDS.sleep(2);

    test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue(test1 == null && test2 == null);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }


  @Test
  public void hasKey() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test1", 1, 1);
    RedisUtils.hSet(RedisKeyConstants.DEMO1, "test2", 2);

    boolean b = RedisUtils.hasKey(RedisKeyConstants.DEMO1);
    boolean b1 = RedisUtils.hasKey(RedisKeyConstants.DEMO2);

    Assert.assertTrue(b);
    Assert.assertFalse(b1);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);


  }


  @Test
  public void hmGet() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.hmSet(RedisKeyConstants.DEMO1, Maps.newHashMap("test1", 1), 1);
    RedisUtils.hmSet(RedisKeyConstants.DEMO1, Maps.newHashMap("test2", 2));

    Map<Object, Object> objectObjectMap = RedisUtils.hmGet(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objectObjectMap.size() == 2);
    Assert.assertTrue((Integer) objectObjectMap.get("test2") == 2);
    Assert.assertTrue((Integer) objectObjectMap.get("test1") == 1);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  @Test
  public void hmSet() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, Map<String, void> map, void time
    //RedisKeyConstants key, Map<String, void> map

    RedisUtils.hmSet(RedisKeyConstants.DEMO1, Maps.newHashMap("test1", 1), 1);
    RedisUtils.hmSet(RedisKeyConstants.DEMO1, Maps.newHashMap("test2", 2));

    Object test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    Object test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue((Integer) test1 == 1 && (Integer) test2 == 2);

    TimeUnit.SECONDS.sleep(2);

    test1 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test1");
    test2 = RedisUtils.hGet(RedisKeyConstants.DEMO1, "test2");

    Assert.assertTrue(test1 == null && test2 == null);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void incr() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void delta
    long incr = RedisUtils.incr(RedisKeyConstants.DEMO1, 1);
    Assert.assertTrue(incr == 1);

    incr = RedisUtils.incr(RedisKeyConstants.DEMO1, 1);
    Assert.assertTrue(incr == 2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }

  /**
   * 获取list缓存的内容
   */
  @Test
  public void lGet() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void start, void end
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);

    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));

    List<Object> objects = RedisUtils.lGet(RedisKeyConstants.DEMO1, 1, 1);

    Assert.assertTrue(objects.get(0).equals("2"));

    objects = RedisUtils.lGet(RedisKeyConstants.DEMO1, 0, -1);
    Assert.assertTrue(objects.size() == 3);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);


  }

  @Test
  public void lGetIndex() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void index
    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));

    Object objects = RedisUtils.lGetIndex(RedisKeyConstants.DEMO1, 1);

    Assert.assertTrue(objects.equals("2"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void lGetListSize() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));

    long objects = RedisUtils.lGetListSize(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects == 3);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void lRemove() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void count, void value

    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));

    long objects = RedisUtils.lRemove(RedisKeyConstants.DEMO1, 1, "1");

    Assert.assertTrue(objects == 1);

    List<Object> objects1 = RedisUtils.lGet(RedisKeyConstants.DEMO1, 0, Integer.MAX_VALUE);

    Assert.assertTrue(objects1.size() == 2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO1);
  }


  @Test
  public void lSet() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, List<void> value
    //  RedisKeyConstants key, List<void> value, void time
    ////    RedisKeyConstants key, void value, void time
    //    RedisKeyConstants key, void value

    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));
    RedisUtils.lSet(RedisKeyConstants.DEMO1, 4);
    RedisUtils.lSet(RedisKeyConstants.DEMO1, "5", 3);
    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("6", "7", "8"), 5);

    long l = RedisUtils.lGetListSize(RedisKeyConstants.DEMO1);

    Assert.assertTrue(l == 8);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

  }

  @Test
  public void lUpdateIndex() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void index, void value

    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("1", "2", "3"));
    RedisUtils.lSet(RedisKeyConstants.DEMO1, "4");
    RedisUtils.lSet(RedisKeyConstants.DEMO1, "5");
    RedisUtils.lSet(RedisKeyConstants.DEMO1, Lists.newArrayList("6", "7", "8"), 5);

    RedisUtils.lUpdateIndex(RedisKeyConstants.DEMO1, 5, "55");

    Object o = RedisUtils.lGetIndex(RedisKeyConstants.DEMO1, 5);

    Assert.assertTrue(o.equals("55"));

    RedisUtils.lUpdateIndex(RedisKeyConstants.DEMO1, 8, "88");
    o = RedisUtils.lGetIndex(RedisKeyConstants.DEMO1, 8);

    Assert.assertTrue(o == null);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void lock() throws InterruptedException {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

    //锁资源 写法1
    CacheLock lock = RedisUtils.getLock(RedisKeyConstants.DEMO1);
    boolean b = lock.tryLock(100, TimeUnit.SECONDS);
    Assert.assertTrue(b);
    lock.unlock();

    //try-with-resource 写法2
    try (CacheLock lock2 = RedisUtils.getLock(RedisKeyConstants.DEMO1)) {
      boolean b1 = lock2.tryLock();
      Assert.assertTrue(b1);
      TimeUnit.SECONDS.sleep(1);
    }

    try (CacheLock lock3 = RedisUtils.getLock(RedisKeyConstants.DEMO1)) {
      boolean isLock = lock3.tryLock();
      Assert.assertTrue(isLock);
      isLock = lock3.tryLock();
      Assert.assertFalse(isLock);
    }

    CacheLock lock2 = RedisUtils.getLock(RedisKeyConstants.DEMO1);
    CacheLock lock3 = RedisUtils.getLock(RedisKeyConstants.DEMO1);
    boolean b2 = lock2.tryLock();
    Assert.assertTrue(b2);
    boolean b3 = lock3.tryLock();
    Assert.assertFalse(b3);
    lock2.unlock();
    b3 = lock3.tryLock();
    Assert.assertTrue(b3);
    lock3.unlock();

    CacheLock lock4 = RedisUtils.getLock(RedisKeyConstants.DEMO1);
    boolean b1 = lock4.tryLock(1, TimeUnit.SECONDS);
    Assert.assertTrue(b1);
    TimeUnit.SECONDS.sleep(2);
    b1 = lock4.tryLock(1, TimeUnit.SECONDS);
    Assert.assertTrue(b1);
    lock4.unlock();

    CacheLock lock11 = RedisUtils.getLock(RedisKeyConstants.DEMO1);
    CacheLock lock22 = RedisUtils.getLock(RedisKeyConstants.DEMO2);
    boolean b4 = lock11.tryLock();
    boolean b5 = lock22.tryLock();
    Assert.assertTrue(b4 && b5);
    lock11.unlock();
    lock22.unlock();

    try (CacheLock lock111 = RedisUtils.getLock(RedisKeyConstants.DEMO1)) {
      try (CacheLock lock221 = RedisUtils.getLock(RedisKeyConstants.DEMO2)) {
        boolean b42 = lock111.tryLock();
        boolean b52 = lock221.tryLock();
        Assert.assertTrue(b42 && b52);
      }
    }

    IntStream.range(0, 10).forEach(i -> {
      new Thread(() -> {
        try (CacheLock lock111 = RedisUtils.getLock(RedisKeyConstants.DEMO1)) {
          try (CacheLock lock221 = RedisUtils.getLock(RedisKeyConstants.DEMO2)) {
            boolean b42 = lock111.tryLock();
            boolean b52 = lock221.tryLock();
            System.out.println("b42--" + b42);
            System.out.println("b52--" + b52);
          }
        }
      }).start();
    });

    RedisKeyConstants redisKeyConstants = RedisKeyConstants.get(RedisKeyConstants.DEMO1, UUID.randomUUID().toString());
    CacheLock lock1 = RedisUtils.getLock(redisKeyConstants);
    lock1.tryLock();
    RedisUtils.del(redisKeyConstants);
    TimeUnit.SECONDS.sleep(5);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void sGet() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key

    RedisUtils.sSet(RedisKeyConstants.DEMO1, "1", "2");
    Set<Object> objects = RedisUtils.sGet(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects.size() == 2 && objects.contains("1") && objects.contains("2"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

  }

  /**
   * 获取set缓存的长度
   *
   * @return
   */
  @Test
  public void sGetSetSize() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key

    RedisUtils.sSet(RedisKeyConstants.DEMO1, "1", "2");
    Long objects = RedisUtils.sGetSetSize(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects == 2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void sHasKey() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
//    RedisKeyConstants key

    RedisUtils.sSet(RedisKeyConstants.DEMO1, "1", "2");
    boolean b1 = RedisUtils.sHasKey(RedisKeyConstants.DEMO1, "1");
    boolean b2 = RedisUtils.sHasKey(RedisKeyConstants.DEMO1, "3");

    Assert.assertTrue(b1);
    Assert.assertFalse(b2);

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void sSet() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void... values

//    RedisKeyConstants key

    RedisUtils.sSet(RedisKeyConstants.DEMO1, "1", "2");
    Set<Object> objects = RedisUtils.sGet(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects.size() == 2 && objects.contains("1") && objects.contains("2"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void sSetAndTime() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void time, void... values


    RedisUtils.sSetAndTime(RedisKeyConstants.DEMO1, 1, "1", "2");
    Set<Object> objects = RedisUtils.sGet(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects.size() == 2 && objects.contains("1") && objects.contains("2"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void set() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void value, void time
    //RedisKeyConstants key, void value


    RedisUtils.set(RedisKeyConstants.DEMO1, "11111");
    Object objects = RedisUtils.get(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects.equals("11111"));

    User user = new User();
    user.setVersion(0);
    user.setEmail("ssssssssssssss");
    RedisUtils.set(RedisKeyConstants.DEMO2, user);
    Object objects2 = RedisUtils.get(RedisKeyConstants.DEMO2);

    Assert.assertTrue(((User) objects2).getEmail().equalsIgnoreCase("ssssssssssssss"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }

  @Test
  public void setRemove() {
    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);

//    RedisKeyConstants key, void... values

    RedisUtils.sSet(RedisKeyConstants.DEMO1, "1111", "2222");
    RedisUtils.setRemove(RedisKeyConstants.DEMO1, "1111");

    Set<Object> objects = RedisUtils.sGet(RedisKeyConstants.DEMO1);

    Assert.assertTrue(objects.size() == 1 && !objects.contains("1111") && objects.contains("2222"));

    RedisUtils.del(RedisKeyConstants.DEMO1, RedisKeyConstants.DEMO2);
  }
}
