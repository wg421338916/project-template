package com.wanggang.template.commons.utils.retry;

import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 重试机制
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:19
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class RetryUtils {
    private static final int INITIAL_SECONDS = 4;
    private static final int MAX_TRY_COUNT = 20;
    private static final int MAX_TRY_DAYS = 7;
    private static final double MULTIPLIER = 1.8;

    private RetryUtils() {
    }

    /**
     * 非持久化，指数重试队列
     * <p>
     * 重试次数0,距离第一次重试间隔0秒,重试间隔0秒
     * 重试次数1,距离第一次重试间隔4.0秒,重试间隔4.0秒
     * 重试次数2,距离第一次重试间隔11.2秒,重试间隔7.2秒
     * 重试次数3,距离第一次重试间隔24.16秒,重试间隔12.96秒
     * 重试次数4,距离第一次重试间隔47.488秒,重试间隔23.328秒
     * 重试次数5,距离第一次重试间隔1.4913分,重试间隔41.99秒
     * 重试次数6,距离第一次重试间隔2.751分,重试间隔1.2597分
     * 重试次数7,距离第一次重试间隔5.018466666666667分,重试间隔2.2674666666666665分
     * 重试次数8,距离第一次重试间隔9.099933333333333分,重试间隔4.081466666666667分
     * 重试次数9,距离第一次重试间隔16.446566666666666分,重试间隔7.346633333333333分
     * 重试次数10,距离第一次重试间隔29.670516666666668分,重试间隔13.22395分
     * 重试次数11,距离第一次重试间隔53.473616666666665分,重试间隔23.8031分
     * 重试次数12,距离第一次重试间隔1.6053202777777777时,重试间隔42.8456分
     * 重试次数13,距离第一次重试间隔2.8906883333333333时,重试间隔1.2853680555555556时
     * 重试次数14,距离第一次重试间隔5.204350833333334时,重试间隔2.3136625时
     * 重试次数15,距离第一次重试间隔9.368943611111112时,重试间隔4.164592777777778时
     * 重试次数16,距离第一次重试间隔16.865210833333332时,重试间隔7.496267222222222时
     * 重试次数17,距离第一次重试间隔1.2649371643518519天,重试间隔13.493281111111111时
     * 重试次数18,距离第一次重试间隔2.276933252314815天,重试间隔1.011996087962963天
     * 重试次数19,距离第一次重试间隔4.0985262152777775天,重试间隔1.821592962962963天
     * 重试次数20,距离第一次重试间隔7.377393553240741天,重试间隔3.278867337962963天
     * 如果重试20次还失败，会放到失败的数据db中，需要手动check
     *
     * @param queueName 队列名称，需要全局唯一
     * @param <T>       对象类型
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getExponentialMemoryQueue(String queueName) {
        Assert.notNull(queueName, "队列名称不能为NULL");

        RetryPolicy policy = new ExponentialRetryPolicy(MAX_TRY_COUNT, TimeUnit.SECONDS.toMillis(INITIAL_SECONDS), TimeUnit.DAYS.toMillis(MAX_TRY_DAYS), MULTIPLIER);
        return new MemoryDispatcherQueue<>(queueName, policy);
    }

    /**
     * 持久化，指数重试队列
     * <p>
     * 重试次数0,距离第一次重试间隔0秒,重试间隔0秒
     * 重试次数1,距离第一次重试间隔4.0秒,重试间隔4.0秒
     * 重试次数2,距离第一次重试间隔11.2秒,重试间隔7.2秒
     * 重试次数3,距离第一次重试间隔24.16秒,重试间隔12.96秒
     * 重试次数4,距离第一次重试间隔47.488秒,重试间隔23.328秒
     * 重试次数5,距离第一次重试间隔1.4913分,重试间隔41.99秒
     * 重试次数6,距离第一次重试间隔2.751分,重试间隔1.2597分
     * 重试次数7,距离第一次重试间隔5.018466666666667分,重试间隔2.2674666666666665分
     * 重试次数8,距离第一次重试间隔9.099933333333333分,重试间隔4.081466666666667分
     * 重试次数9,距离第一次重试间隔16.446566666666666分,重试间隔7.346633333333333分
     * 重试次数10,距离第一次重试间隔29.670516666666668分,重试间隔13.22395分
     * 重试次数11,距离第一次重试间隔53.473616666666665分,重试间隔23.8031分
     * 重试次数12,距离第一次重试间隔1.6053202777777777时,重试间隔42.8456分
     * 重试次数13,距离第一次重试间隔2.8906883333333333时,重试间隔1.2853680555555556时
     * 重试次数14,距离第一次重试间隔5.204350833333334时,重试间隔2.3136625时
     * 重试次数15,距离第一次重试间隔9.368943611111112时,重试间隔4.164592777777778时
     * 重试次数16,距离第一次重试间隔16.865210833333332时,重试间隔7.496267222222222时
     * 重试次数17,距离第一次重试间隔1.2649371643518519天,重试间隔13.493281111111111时
     * 重试次数18,距离第一次重试间隔2.276933252314815天,重试间隔1.011996087962963天
     * 重试次数19,距离第一次重试间隔4.0985262152777775天,重试间隔1.821592962962963天
     * 重试次数20,距离第一次重试间隔7.377393553240741天,重试间隔3.278867337962963天
     * 如果重试20次还失败，会放到失败的数据db中，需要手动check
     *
     * @param queueName 队列名称，需要全局唯一
     * @param <T>       对象类型
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getExponentialPersistenceQueue(String queueName) throws IOException {
        Assert.notNull(queueName, "队列名称不能为NULL");

        RetryPolicy policy = new ExponentialRetryPolicy(MAX_TRY_COUNT, TimeUnit.SECONDS.toMillis(INITIAL_SECONDS), TimeUnit.DAYS.toMillis(MAX_TRY_DAYS), MULTIPLIER);
        return new PersistenceDispatcherQueue<>(queueName, policy);
    }

    /**
     * 非持久化，固定时间重试队列
     * 间隔4s重试一次，最大重试20次
     *
     * @param queueName 队列名称，需要全局唯一
     * @param <T>       对象类型
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getFixedMemoryQueue(String queueName) {
        Assert.notNull(queueName, "队列名称不能为NULL");

        RetryPolicy policy = new FixedPolicy(MAX_TRY_COUNT, TimeUnit.SECONDS.toMillis(INITIAL_SECONDS));
        return new MemoryDispatcherQueue<>(queueName, policy);
    }

    /**
     * 持久化，固定时间重试队列
     * 间隔4s重试一次，最大重试20次
     *
     * @param queueName 队列名称，需要全局唯一
     * @param <T>       对象类型
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getFixedPersistenceQueue(String queueName) throws IOException {
        Assert.notNull(queueName, "队列名称不能为NULL");

        RetryPolicy policy = new FixedPolicy(MAX_TRY_COUNT, TimeUnit.SECONDS.toMillis(INITIAL_SECONDS));
        return new PersistenceDispatcherQueue<>(queueName, policy);
    }

    /**
     * 非持久化，指定策略重试队列
     *
     * @param queueName   队列名称，需要全局唯一
     * @param <T>         对象类型
     * @param retryPolicy 重试策略
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getMemoryQueue(String queueName, RetryPolicy retryPolicy) {
        Assert.notNull(queueName, "队列名称不能为NULL");
        Assert.notNull(retryPolicy, "策略不能为NULL");

        return new MemoryDispatcherQueue<>(queueName, retryPolicy);
    }

    /**
     * 持久化，指定策略重试队列
     *
     * @param queueName   队列名称，需要全局唯一
     * @param <T>         对象类型
     * @param retryPolicy 重试策略
     * @return 队列
     * @throws IOException leveldb需要io操作
     */
    public static <T> DispatcherQueue<T> getPersistenceQueue(String queueName, RetryPolicy retryPolicy) throws IOException {
        Assert.notNull(queueName, "队列名称不能为NULL");
        Assert.notNull(retryPolicy, "策略不能为NULL");

        return new PersistenceDispatcherQueue<>(queueName, retryPolicy);
    }
}
