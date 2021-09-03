package com.wanggang.template.commons.utils.retry;

import java.util.function.Predicate;


/**
 * 重试调度队列
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:31
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public interface DispatcherQueue<T> {
    /**
     * 添加需要重试的实体
     *
     * @param retryContent
     * @return 是否加入成功
     */
    Boolean put(T retryContent);

    /**
     * 添加需要重试的实体
     *
     * @param retryContent 内容
     * @param delayMillis  延迟执行,单位毫秒
     * @param canRepeat    允许重复
     * @return 是否加入成功
     */
    Boolean put(T retryContent, Long delayMillis, Boolean canRepeat);

    /**
     * 注册需要执行的函数,一个队列只允许注册一次
     *
     * @param handler 具体执行的函数
     */
    void register(Predicate<RetryWrapperMessage<T>> handler);
}

