package com.aegis.template.commons.utils.cache.redis;

import cn.hutool.core.lang.UUID;
import com.aegis.template.commons.constants.RedisKeyConstants;
import com.aegis.template.commons.utils.cache.CacheLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 分布式锁
 *
 * @author 秦腾
 * @version 1.0
 * @date 2020/4/16 16:50
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
final class RedisLockImpl implements CacheLock {
  private static final Long LOCK_EXPIRED = -1L;
  private static final Long LOCK_SUCCESS = 1L;
  private RedisKeyConstants key;
  /**
   * 定义获取锁的lua脚本
   */
  private DefaultRedisScript<Long> lockLua = new DefaultRedisScript<>(
      "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end"
      , Long.class
  );
  private StringRedisTemplate redisTemplate;
  /**
   * 定义获取锁的lua脚本
   */
  private DefaultRedisScript<Long> unlockLua = new DefaultRedisScript<>(
      "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return -1 end"
      , Long.class
  );
  private String value;

  public RedisLockImpl(RedisKeyConstants key, StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.key = key;
    this.value = UUID.randomUUID().toString();
  }

  @Override
  public void unlock() {
    unlock(key, value);
  }

  /**
   * 释放锁
   *
   * @param key   释放本请求对应的key
   * @param value 释放本请求对应的锁的value
   * @return boolean 是否释放锁成功
   */
  private boolean unlock(RedisKeyConstants key, String value) {
    try {
      // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
      Object result = redisTemplate.execute(unlockLua, Collections.singletonList(key.getKey()), value);
      // 如果这里抛异常，后续锁无法释放
      if (LOCK_SUCCESS.equals(result)) {
        log.info("解锁成功,key:{},value:{},threadName:{},结果:{}", key, value, Thread.currentThread().getName(), result);
        return true;
      }

      if (LOCK_EXPIRED.equals(result)) {
        // 返回-1说明获取到的KEY值与requestId不一致或者KEY不存在，可能已经过期或被其他线程加锁
        // 一般发生在key的过期时间短于业务处理时间，属于正常可接受情况
        log.info("解锁成功,超时自动释放,key:{},value:{},threadName:{},结果:{}", key, value, Thread.currentThread().getName(), result);
        return true;
      }

      return false;
    } catch (Exception e) {
      log.error(String.format("REDIS解锁异常,key:%s,val:%s,threadName:%s", key.getKey(), value, Thread.currentThread().getName()), e);
    }
    return false;
  }

  @Override
  public boolean tryLock() {
    return tryLock(30, TimeUnit.SECONDS);
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) {
    return lock(key, value, unit.toMillis(time));
  }

  /**
   * 加锁
   *
   * @param key     redis键值对的key
   * @param value   redis键值对的value 随机串作为值
   * @param timeout redis键值对的过期时间 毫秒为单位
   * @return boolean 是否加锁成功
   */
  private boolean lock(RedisKeyConstants key, String value, Long timeout) {
    try {
      List<String> keys = Collections.singletonList(key.getKey());
      // 执行脚本
      Object result = redisTemplate.execute(lockLua, keys, value, timeout.toString());
      // 存储本地变量
      if (LOCK_SUCCESS.equals(result)) {
        log.info("REDIS加锁成功,key:{},val:{},threadName:{},结果:{}", key.getKey(), value, Thread.currentThread().getName(), result);
        return true;
      }
    } catch (Exception e1) {
      log.error(String.format("REDIS加锁异常,key:%s,val:%s,threadName:%s", key.getKey(), value, Thread.currentThread().getName()), e1);
    }
    return false;
  }
}
