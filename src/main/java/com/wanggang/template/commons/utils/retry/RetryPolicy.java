package com.wanggang.template.commons.utils.retry;

/**
 * 重试策略
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:29
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public interface RetryPolicy {
    /**
     * 是否可以重试
     *
     * @param retryCount 重试次数
     * @return true 是可以，false 是不可以
     */
    Boolean canTry(Integer retryCount);

    /**
     * 的到延迟时间
     *
     * @param retryCount 重试次数
     * @return
     */
    Long getDelayTime(Integer retryCount);
}
