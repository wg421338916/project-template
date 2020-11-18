package com.aegis.template.commons.utils.cache.redis;


import cn.hutool.core.lang.Assert;
import com.aegis.template.commons.constants.RedisKeyConstants;
import com.aegis.template.commons.utils.ApplicationContextUtils;
import com.aegis.template.commons.utils.cache.CacheLock;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtils 帮助类
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/14 17:00
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class RedisUtils {

  private static final String KEY_NOT_NULL = "REDIS KEY 不能为NULL";
  private static final String VALUE_NOT_NULL = "REDIS VALUE 不能为NULL";
  private static RedisTemplate<RedisKeyConstants, Object> redisTemplate;
  private static StringRedisTemplate stringRedisTemplate;

  static {
    redisTemplate = (RedisTemplate<RedisKeyConstants, Object>) ApplicationContextUtils.getBean("redisTemplate");
    stringRedisTemplate = ApplicationContextUtils.getBean(StringRedisTemplate.class);
  }

  private RedisUtils() {
  }

  /**
   * 递减
   *
   * @param key   键
   * @param delta 要减少几(大于0,否则重置为1)
   * @return
   */
  public static long decr(RedisKeyConstants key, long delta) {
    Assert.notNull(key, KEY_NOT_NULL);

    if (delta > 0) {
      delta = 1;
    }

    return redisTemplate.opsForValue().increment(key, -delta);
  }

  /**
   * 删除缓存
   *
   * @param keys 可以传一个值 或多个
   */
  @SuppressWarnings("unchecked")
  public static void del(RedisKeyConstants... keys) {
    if (ArrayUtils.isEmpty(keys)) {
      return;
    }

    if (keys.length == 1) {
      redisTemplate.delete(keys[0]);
      return;
    }

    redisTemplate.delete(CollectionUtils.arrayToList(keys));
  }

  /**
   * 普通缓存获取
   *
   * @param key 键
   * @return 值
   */
  public static Object get(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    return redisTemplate.opsForValue().get(key);
  }

  /**
   * 根据key 获取过期时间
   *
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效
   */
  public static long getExpire(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  /**
   * 获取锁
   *
   * @param key 要锁的key值
   * @return 锁
   */
  public static CacheLock getLock(RedisKeyConstants key) {
    return new RedisLockImpl(key, stringRedisTemplate);
  }

  /**
   * hash递减
   *
   * @param key  键
   * @param item 项
   * @param by   要减少的数值
   * @return
   */
  public static double hDecr(RedisKeyConstants key, String item, double by) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);

    return redisTemplate.opsForHash().increment(key, item, -by);
  }

  /**
   * 删除hash表中的值
   *
   * @param key  键 不能为null
   * @param item 项 可以使多个 不能为null
   */
  public static void hDel(RedisKeyConstants key, Object... item) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, VALUE_NOT_NULL);

    redisTemplate.opsForHash().delete(key, item);
  }

  /**
   * HashGet
   *
   * @param key  键 不能为null
   * @param item 项 不能为null
   * @return 值
   */
  public static Object hGet(RedisKeyConstants key, String item) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);

    return redisTemplate.opsForHash().get(key, item);
  }

  /**
   * 判断hash表中是否有该项的值
   *
   * @param key  键 不能为null
   * @param item 项 不能为null
   * @return true 存在 false不存在
   */
  public static boolean hHasKey(RedisKeyConstants key, String item) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);

    return redisTemplate.opsForHash().hasKey(key, item);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回
   *
   * @param key  键
   * @param item 项
   * @param by   要增加几(大于0)
   * @return
   */
  public static double hIncr(RedisKeyConstants key, String item, double by) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);

    return redisTemplate.opsForHash().increment(key, item, by);
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key   键
   * @param item  项
   * @param value 值
   * @return true 成功 false失败
   */
  public static boolean hSet(RedisKeyConstants key, String item, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForHash().put(key, item, value);
      return true;
    } catch (Exception e) {
      log.warn("redis hSet error", e);
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key     键
   * @param item    项
   * @param value   值
   * @param keyTime 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
   * @return true 成功 false失败
   */
  public static boolean hSet(RedisKeyConstants key, String item, Object value, long keyTime) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(item, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForHash().put(key, item, value);
      if (keyTime > 0) {
        expire(key, keyTime);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis hSet error", e);
      return false;
    }
  }

  /**
   * 指定缓存失效时间
   *
   * @param key  键
   * @param time 时间(秒)
   * @return
   */
  public static boolean expire(RedisKeyConstants key, long time) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      if (time > 0) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis expire error", e);
      return false;
    }
  }

  /**
   * 判断key是否存在
   *
   * @param key 键
   * @return true 存在 false不存在
   */
  public static boolean hasKey(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      log.warn("redis hasKey error", e);
      return false;
    }
  }

  /**
   * 获取hashKey对应的所有键值
   *
   * @param key 键
   * @return 对应的多个键值
   */
  public static Map<Object, Object> hmGet(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    return redisTemplate.opsForHash().entries(key);
  }

  /**
   * HashSet
   *
   * @param key 键
   * @param map 对应多个键值
   * @return true 成功 false 失败
   */
  public static boolean hmSet(RedisKeyConstants key, Map<String, Object> map) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(map, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      log.warn("redis hmSet error", e);
      return false;
    }
  }

  /**
   * HashSet 并设置时间
   *
   * @param key  键
   * @param map  对应多个键值
   * @param time 时间(秒)
   * @return true成功 false失败
   */
  public static boolean hmSet(RedisKeyConstants key, Map<String, Object> map, long time) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(map, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForHash().putAll(key, map);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis hmSet error", e);
      return false;
    }
  }

  /**
   * 递增
   *
   * @param key   键
   * @param delta 要增加几(大于0,否则重置为1)
   * @return
   */
  public static long incr(RedisKeyConstants key, long delta) {
    Assert.notNull(key, KEY_NOT_NULL);

    if (delta < 0) {
      delta = 1;
    }

    return redisTemplate.opsForValue().increment(key, delta);
  }

  /**
   * 获取list缓存的内容
   *
   * @param key   键
   * @param start 开始
   * @param end   结束  0 到 -1代表所有值
   * @return
   */
  public static List<Object> lGet(RedisKeyConstants key, long start, long end) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.isTrue(start >= 0, "开始位置不能小于0");

    try {
      return redisTemplate.opsForList().range(key, start, end);
    } catch (Exception e) {
      log.warn("redis lGet error", e);
      return Lists.newArrayList();
    }
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key   键
   * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return
   */
  public static Object lGetIndex(RedisKeyConstants key, long index) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.opsForList().index(key, index);
    } catch (Exception e) {
      log.warn("redis lGetIndex error", e);
      return null;
    }
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return
   */
  public static long lGetListSize(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.opsForList().size(key);
    } catch (Exception e) {
      log.warn("redis lGetListSize error", e);
      return 0;
    }
  }

  /**
   * 移除N个值为value
   *
   * @param key   键
   * @param count 移除多少个
   * @param value 值
   * @return 移除的个数
   */
  public static long lRemove(RedisKeyConstants key, long count, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.opsForList().remove(key, count, value);
    } catch (Exception e) {
      log.warn("redis lRemove error", e);
      return 0;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public static boolean lSet(RedisKeyConstants key, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForList().rightPush(key, value);
      return true;
    } catch (Exception e) {
      log.warn("redis lSet error kv=" + key + ":" + value, e);
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @param time  时间(秒)
   * @return
   */
  public static boolean lSet(RedisKeyConstants key, Object value, long time) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForList().rightPush(key, value);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis lSet error kvt=" + key + ":" + value + ":" + time, e);
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public static boolean lSet(RedisKeyConstants key, List<Object> value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForList().rightPushAll(key, value);
      return true;
    } catch (Exception e) {
      log.warn("redis lSet error key:" + key, e);
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @param time  时间(秒)
   * @return
   */
  public static boolean lSet(RedisKeyConstants key, List<Object> value, long time) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notEmpty(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForList().rightPushAll(key, value);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis lSet error", e);
      return false;
    }
  }

  /**
   * 根据索引修改list中的某条数据
   *
   * @param key   键
   * @param index 索引
   * @param value 值
   * @return
   */
  public static boolean lUpdateIndex(RedisKeyConstants key, long index, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.isTrue(index >= 0, "REDIS索引越界");

    try {
      redisTemplate.opsForList().set(key, index, value);
      return true;
    } catch (Exception e) {
      log.warn("REDIS索引越界", e);
      return false;
    }
  }

  /**
   * 根据key获取Set中的所有值
   *
   * @param key 键
   * @return
   */
  public static Set<Object> sGet(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.opsForSet().members(key);
    } catch (Exception e) {
      log.warn("redis sGet error", e);
      return Sets.newHashSet();
    }
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return
   */
  public static long sGetSetSize(RedisKeyConstants key) {
    Assert.notNull(key, KEY_NOT_NULL);

    try {
      return redisTemplate.opsForSet().size(key);
    } catch (Exception e) {
      log.warn("redis sGetSetSize error", e);
      return 0;
    }
  }

  /**
   * 根据value从一个set中查询,是否存在
   *
   * @param key   键
   * @param value 值
   * @return true 存在 false不存在
   */
  public static boolean sHasKey(RedisKeyConstants key, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      return redisTemplate.opsForSet().isMember(key, value);
    } catch (Exception e) {
      log.warn("redis sHasKey error", e);
      return false;
    }
  }

  /**
   * 将数据放入set缓存
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long sSet(RedisKeyConstants key, Object... values) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notEmpty(values, VALUE_NOT_NULL);

    try {
      return redisTemplate.opsForSet().add(key, values);
    } catch (Exception e) {
      log.warn("redis sSet error", e);
      return 0;
    }
  }

  /**
   * 将set数据放入缓存
   *
   * @param key    键
   * @param time   时间(秒)
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long sSetAndTime(RedisKeyConstants key, long time, Object... values) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notEmpty(values, VALUE_NOT_NULL);

    try {
      Long count = redisTemplate.opsForSet().add(key, values);
      if (time > 0) {
        expire(key, time);
      }
      return count;
    } catch (Exception e) {
      log.warn("redis sSetAndTime error", e);
      return 0;
    }
  }

  /**
   * 普通缓存放入并设置时间
   *
   * @param key   键
   * @param value 值
   * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  public static boolean set(RedisKeyConstants key, Object value, long time) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      if (time > 0) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      log.warn("redis set error", e);
      return false;
    }
  }

  /**
   * 普通缓存放入
   *
   * @param key   键
   * @param value 值
   * @return true成功 false失败
   */
  public static boolean set(RedisKeyConstants key, Object value) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notNull(value, VALUE_NOT_NULL);

    try {
      redisTemplate.opsForValue().set(key, value);
      return true;
    } catch (Exception e) {
      log.warn("redis error", e);
      return false;
    }
  }

  /**
   * 移除值为value的
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  public static long setRemove(RedisKeyConstants key, Object... values) {
    Assert.notNull(key, KEY_NOT_NULL);
    Assert.notEmpty(values, VALUE_NOT_NULL);

    try {
      return redisTemplate.opsForSet().remove(key, values);
    } catch (Exception e) {
      log.warn("redis error", e);
      return 0;
    }
  }

}
