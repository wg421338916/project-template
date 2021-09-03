package com.wanggang.template.commons.utils.retry;

import java.util.List;

/**
 * 存错服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 11:38
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public interface StoreService<T> {
    /**
     * 添加消息到持久化层
     *
     * @param retryMessage 需要重试的消息
     */
    default void add(RetryMessage<T> retryMessage) {
    }

    /**
     * 根据id删除消息
     *
     * @param id 消息id
     */
    default void delete(String id) {
    }

    /**
     * 查询消息
     *
     * @return 返回所有的消息
     */
    List<RetryMessage<T>> queryAll();

    /**
     * 更新消息
     *
     * @param retryMessage 需要重试的消息
     */
    default void update(RetryMessage<T> retryMessage) {
    }
}
