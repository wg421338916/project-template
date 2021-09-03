package com.wanggang.template.commons.utils.retry;

/**
 * 固定时间间隔重试
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/17 18:06
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class FixedPolicy implements RetryPolicy {
    private long initialInterval;

    private long maxTryCount;


    public FixedPolicy(Integer maxTryCount, long initialInterval) {
        this.maxTryCount = maxTryCount;
        this.initialInterval = initialInterval;
    }

    @Override
    public Boolean canTry(Integer retryCount) {
        return maxTryCount >= retryCount;
    }

    @Override
    public Long getDelayTime(Integer retryCount) {
        return System.currentTimeMillis() + initialInterval;
    }
}
