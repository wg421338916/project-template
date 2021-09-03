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
public class ComplexPolicy implements RetryPolicy {

    private RetryPolicy retryPolicy;


    public ComplexPolicy(Integer maxTryCount, long initialInterval, long maxInterval, double multiplier) {
        if (maxTryCount <= 0) {
            maxTryCount = 0;
        }

        if (multiplier <= 1 || maxInterval <= 0) {
            retryPolicy = new FixedPolicy(maxTryCount, initialInterval);
        } else {
            retryPolicy = new ExponentialRetryPolicy(maxTryCount, initialInterval, maxInterval, multiplier);
        }
    }

    @Override
    public Boolean canTry(Integer retryCount) {
        return retryPolicy.canTry(retryCount);
    }

    @Override
    public Long getDelayTime(Integer retryCount) {
        return retryPolicy.getDelayTime(retryCount);
    }
}