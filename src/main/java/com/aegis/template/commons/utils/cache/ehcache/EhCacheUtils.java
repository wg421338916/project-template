package com.aegis.template.commons.utils.cache.ehcache;


import com.aegis.template.commons.utils.ApplicationContextUtils;
import com.aegis.template.commons.utils.cache.CacheLock;
import net.sf.ehcache.Ehcache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;

import java.util.List;

/**
 * EhCache 帮助类
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 17:00
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class EhCacheUtils {
  private static final String SYS_CACHE = "sysCache";
  private static CacheManager cacheManager;

  static {
    cacheManager = ((CacheManager) ApplicationContextUtils.getBean("ehCacheCacheManager"));
  }

  private EhCacheUtils() {
  }

  public static void flush(String cacheName) {
    getEhCache(cacheName).flush();
  }

  private static Ehcache getEhCache(String cacheName) {
    return (Ehcache) cacheManager.getCache(cacheName).getNativeCache();
  }

  /**
   * 获取SYS_CACHE缓存
   *
   * @param key
   * @return
   */
  public static <T> T get(String key, Class<T> type) {
    return get(SYS_CACHE, key, type);
  }

  /**
   * 获取缓存
   *
   * @param cacheName
   * @param key
   * @param type
   * @return
   */
  public static <T> T get(String cacheName, String key, Class<T> type) {
    return getCache(cacheName).get(key, type);
  }

  /**
   * @param cacheName
   * @return
   */
  private static Cache getCache(String cacheName) {
    return cacheManager.getCache(cacheName);
  }

  /**
   * 获取SYS_CACHE缓存
   *
   * @param key
   * @return
   */
  public static Object get(String key) {
    return get(SYS_CACHE, key);
  }

  /**
   * 获取缓存
   *
   * @param cacheName
   * @param key
   * @return
   */
  public static Object get(String cacheName, String key) {
    Cache.ValueWrapper valueWrapper = getCache(cacheName).get(key);
    if (valueWrapper == null) {
      return null;
    }

    return valueWrapper.get();
  }

  /**
   * 获取锁
   *
   * @param cacheName
   * @param key       要锁的key值
   * @return 锁
   */
  public static CacheLock getLock(String cacheName, String key) {

    EhCacheCache cache = (EhCacheCache) getCache(cacheName);

    return new EhCacheLockImpl(cache.getNativeCache(), key);
  }

  /**
   * 获取锁 SYS_CACHE
   *
   * @param key 要锁的key值
   * @return 锁
   */
  public static CacheLock getLock(String key) {
    EhCacheCache cache = (EhCacheCache) getCache(SYS_CACHE);

    return new EhCacheLockImpl(cache.getNativeCache(), key);
  }


  /**
   * 罗列SYS_CACHE下所有的key值
   *
   * @return
   */
  public static List<String> listKeys() {
    return listKeys(SYS_CACHE);
  }


  /**
   * 罗列 cacheName下所有的key值
   *
   * @param cacheName
   * @return
   */
  public static List<String> listKeys(String cacheName) {
    return getEhCache(cacheName).getKeys();
  }

  /**
   * 写入SYS_CACHE缓存
   *
   * @param key
   * @return
   */
  public static void put(String key, Object value) {
    put(SYS_CACHE, key, value);
  }

  /**
   * 写入缓存
   *
   * @param cacheName
   * @param key
   * @param value
   */
  public static void put(String cacheName, String key, Object value) {
    getCache(cacheName).put(key, value);
  }

  /**
   * 如果不存在写入SYS_CACHE缓存
   *
   * @param key
   * @return
   */
  public static Boolean putIfAbsent(String key, Object value) {
    return putIfAbsent(SYS_CACHE, key, value);
  }

  /**
   * 如果不存在写入缓存
   *
   * @param cacheName
   * @param key
   * @param value
   */
  public static Boolean putIfAbsent(String cacheName, String key, Object value) {
    Cache.ValueWrapper valueWrapper = getCache(cacheName).putIfAbsent(key, value);

    return valueWrapper == null;
  }

  /**
   * @param key
   * @return
   */
  public static void remove(String key) {
    remove(SYS_CACHE, key);
  }

  /**
   * 从缓存中移除
   *
   * @param cacheName
   * @param key
   */
  public static void remove(String cacheName, String key) {
    getCache(cacheName).evict(key);
  }

  public static void removeAll(String cacheName) {
    getCache(cacheName).clear();
  }
}
