package com.aegis.template.commons.utils.cache.ehcache;

import com.aegis.template.commons.utils.cache.CacheLock;
import lombok.SneakyThrows;
import net.sf.ehcache.Ehcache;

import java.util.concurrent.TimeUnit;

/**
 * ehCache 单机锁，可以重入锁
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/13 10:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.8
 */
class EhCacheLockImpl implements CacheLock {

  private Ehcache ehcache;
  private String key;

  public EhCacheLockImpl(Ehcache ehcache, String key) {
    this.ehcache = ehcache;
    this.key = key;
  }

  @Override
  public void unlock() {
    if (ehcache.isWriteLockedByCurrentThread(key)) {
      ehcache.releaseWriteLockOnKey(key);
    }
  }

  @SneakyThrows
  @Override
  public boolean tryLock() {
    return ehcache.tryWriteLockOnKey(key, 0);
  }

  @SneakyThrows
  @Override
  public boolean tryLock(long time, TimeUnit unit) {
    return ehcache.tryWriteLockOnKey(key, unit.toMillis(time));
  }
}
