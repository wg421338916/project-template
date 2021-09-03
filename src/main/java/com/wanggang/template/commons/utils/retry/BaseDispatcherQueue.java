package com.wanggang.template.commons.utils.retry;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;


/**
 * 重试队列基础服务类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:57
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
abstract class BaseDispatcherQueue<T> implements DispatcherQueue<T> {
    private static final int ONE_MINUTE = 1;
    private static final int THREAD_COUNT = 8;
    private static final int WARN_SIZE = 10000;
    private static DelayQueue<RetryMessage<?>> delayQueue = null;
    private static Map<String, BaseDispatcherQueue<?>> dispatcherQueues = null;
    private static AtomicBoolean isInit = new AtomicBoolean(false);
    protected Predicate<RetryWrapperMessage<T>> handler = null;
    protected RetryPolicy retryPolicy;
    protected StoreService<T> storeService;
    private AtomicBoolean isRegister = new AtomicBoolean(false);
    private String queueName;

    public BaseDispatcherQueue(String queueName, StoreService<T> storeService, RetryPolicy retryPolicy) {
        BaseDispatcherQueue.init();

        Assert.notNull(queueName, "队列名称不能为NULL");
        Assert.isNull(BaseDispatcherQueue.dispatcherQueues.get(queueName.toUpperCase()), "重试组件已经存在相同名称的队列,队列名称" + queueName);

        this.storeService = storeService;
        this.retryPolicy = retryPolicy;
        this.queueName = queueName;

        BaseDispatcherQueue.dispatcherQueues.put(queueName.toUpperCase(), this);
    }

    private static void init() {
        if (!isInit.compareAndSet(false, true)) {
            return;
        }

        log.info("【失败重试组件】进行初始化...");

        delayQueue = new DelayQueue<>();
        dispatcherQueues = Maps.newConcurrentMap();
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("dispatcherQueue-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(getThreadCount(), getThreadCount(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());

        executorService.execute(() -> {
            try {
                while (true) {
                    RetryMessage<?> retryMessage = delayQueue.poll(ONE_MINUTE, TimeUnit.MINUTES);
                    if (retryMessage == null) {
                        continue;
                    }

                    if (delayQueue.size() > WARN_SIZE) {
                        log.warn("【失败重试组件】重试队列数据超过1w,size:{}", delayQueue.size());
                    }

                    executorService.execute(() -> {
                        BaseDispatcherQueue<?> baseDispatcherQueue = dispatcherQueues.get(retryMessage.getQueueName().toUpperCase());
                        if (baseDispatcherQueue == null) {
                            return;
                        }

                        log.info("【失败重试组件】开始异步处理数据...,id:{},tryCount:{},data:{}", retryMessage.getId(), retryMessage.getRetryCount(), retryMessage.toString());

                        baseDispatcherQueue.processMessage(retryMessage);

                        log.info("【失败重试组件】异步处理数据完成,data:{}", retryMessage.getId());
                    });
                }
            } catch (InterruptedException e) {
                log.warn("【失败重试组件】重试线程异常", e);
                Thread.currentThread().interrupt();
            }
        });

        log.info("【失败重试组件】进行初始化结束");
    }

    private static int getThreadCount() {
        try {
            return Runtime.getRuntime().availableProcessors() + 1;
        } catch (Exception e) {
            return THREAD_COUNT;
        }
    }

    @SuppressWarnings("unchecked")
    protected void processMessage(RetryMessage<?> retryMessage) {
        if (handler == null || retryMessage == null) {
            return;
        }

        RetryMessage<T> message = (RetryMessage<T>) retryMessage;
        boolean result = false;

        try {
            result = handler.test(new RetryWrapperMessage<T>(message.getQueueName(), message.getId(), message.getRetryCount(), message.getPayload()));
        } catch (Exception e) {
            log.warn("【失败重试组件】业务处理异常", e);
        }

        if (result) {
            processMessageSuccess(message);
            return;
        }

        retryMessage.setRetryCount(retryMessage.getRetryCount() + 1);
        Long delayTime = retryPolicy.getDelayTime(retryMessage.getRetryCount());
        retryMessage.setDelayTime(delayTime);
        processMessageFail(message);
    }

    protected void processMessageSuccess(RetryMessage<T> retryMessage) {
        this.storeService.delete(retryMessage.getId());
        log.info("【失败重试组件】数据处理成功,data:{}", retryMessage.getId());
    }

    /**
     * 业务消息处理失败
     *
     * @param retryMessage 消息重试
     * @return true 代表可以继续重试，false 代表不可以继续重试
     */
    protected boolean processMessageFail(RetryMessage<T> retryMessage) {
        boolean canTry = this.retryPolicy.canTry(retryMessage.getRetryCount());
        if (!canTry) {
            this.storeService.delete(retryMessage.getId());
            log.info("【失败重试组件】数据处理已经达到最大限制，不在重试,dataId:{}", retryMessage.getId());
            return false;
        }

        storeService.update(retryMessage);

        //消息加入到延迟队列中
        delayQueue.put(retryMessage);

        log.info("【失败重试组件】数据处理失败,dataId:{}", retryMessage.getId());

        return true;
    }

    @Override
    public Boolean put(T retryContent) {
        return put(retryContent, 0L, true);
    }

    /**
     * 添加需要重试的实体
     *
     * @param retryContent 内容
     * @param delayMillis  延迟执行
     * @param canRepeat    允许重复
     * @return 是否加入成功
     */
    @Override
    public Boolean put(T retryContent, Long delayMillis, Boolean canRepeat) {
        Assert.notNull(retryContent, "要处理的对象不能为NULL");

        if (this.handler == null) {
            return false;
        }

        String content = StrUtil.EMPTY;
        try {
            RetryMessage<T> objectRetryMessage = new RetryMessage<>(retryContent, this.queueName);
            if (delayMillis > 0) {
                objectRetryMessage.setDelayTime(System.currentTimeMillis() + delayMillis);
            }

            if (Boolean.FALSE.equals(canRepeat) && delayQueue.contains(objectRetryMessage)) {
                log.info("【失败重试组件】重复数据，禁止加入,content:{}", objectRetryMessage.toString());
                return false;
            }

            log.info("【失败重试组件】put数据...,content:{}", objectRetryMessage.toString());

            //消息持久化
            storeService.add(objectRetryMessage);

            //消息加入到延迟队列中
            delayQueue.put(objectRetryMessage);
        } catch (Exception e) {
            log.error("【失败重试组件】put数据失败,data:" + content, e);
            return false;
        }

        return true;
    }

    @Override
    public void register(Predicate<RetryWrapperMessage<T>> handler) {
        Assert.notNull(handler, "【失败重试组件】注册函数不能为NULL");
        Assert.isTrue(isRegister.compareAndSet(false, true), "【失败重试组件】只允许注册一次");

        this.handler = handler;

        try {
            List<RetryMessage<T>> list = storeService.queryAll();
            log.info("【失败重试组件】queueName:{},put历史数据,size:{}", this.queueName, list.size());
            list.forEach(t -> t.setDelayTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30)));
            BaseDispatcherQueue.delayQueue.addAll(list);
        } catch (Exception e) {
            log.error("【失败重试组件】处理老数据出现问题，请检查...", e);
        }
    }
}
