package com.wanggang.template.commons.utils.retry;

/**
 * 指数失败重试策略
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/17 15:02
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class ExponentialRetryPolicy implements RetryPolicy {
    private long initialInterval;
    private long maxInterval;

    private Integer maxTryCount;
    private double multiplier;

    public ExponentialRetryPolicy(Integer maxTryCount, long initialInterval, long maxInterval, double multiplier) {
        this.maxTryCount = maxTryCount;
        this.initialInterval = initialInterval;
        this.multiplier = multiplier;
        this.maxInterval = maxInterval;
    }

    @Override
    public Boolean canTry(Integer retryCount) {
        return maxTryCount >= retryCount;
    }

    @Override
    public Long getDelayTime(Integer retryCount) {
        return System.currentTimeMillis() + getDelayTimeSelf(retryCount);
    }

    private Long getDelayTimeSelf(Integer retryCount) {
        long delay = (long) (Math.pow(multiplier, retryCount - 1d) * initialInterval);
        if (delay > maxInterval) {
            delay = maxInterval;
        }

        return delay;
    }
}


