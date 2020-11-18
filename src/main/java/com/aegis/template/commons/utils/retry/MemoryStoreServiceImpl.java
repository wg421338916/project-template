package com.aegis.template.commons.utils.retry;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * cache 服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/17 15:51
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
class MemoryStoreServiceImpl<T> implements StoreService<T> {
  @Override
  public void add(RetryMessage<T> retryMessage) {
    log.debug("【失败重试组件】内存添加消息，id:{}", retryMessage.getId());
  }

  @Override
  public List<RetryMessage<T>> queryAll() {
    return Lists.newArrayList();
  }
}
