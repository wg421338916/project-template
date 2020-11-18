package com.aegis.template.commons.utils.retry;

/**
 * 内存重试队列
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/17 15:57
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
class MemoryDispatcherQueue<T> extends BaseDispatcherQueue<T> {
  public MemoryDispatcherQueue(String name, RetryPolicy retryPolicy) {
    super(name, new MemoryStoreServiceImpl<>(), retryPolicy);
  }
}
