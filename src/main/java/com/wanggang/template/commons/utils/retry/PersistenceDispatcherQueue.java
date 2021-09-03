package com.wanggang.template.commons.utils.retry;

import com.wanggang.template.commons.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 持久化重试队列
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/27 22:25
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class PersistenceDispatcherQueue<T> extends BaseDispatcherQueue<T> {
    private static final String PATH = GlobalConfig.getProfile() + "/retryDb";
    private static final GenericJackson2JsonRetrySerializer serializer = new GenericJackson2JsonRetrySerializer();
    private StoreService<T> tLevelDbStoreServiceError;

    public PersistenceDispatcherQueue(String name, RetryPolicy retryPolicy) throws IOException {
        super(name, new LevelDbStoreServiceImpl<>(PATH, name, serializer), retryPolicy);

        tLevelDbStoreServiceError = new LevelDbStoreServiceImpl<>(PATH, name + ".err", serializer);
    }

    @Override
    protected boolean processMessageFail(RetryMessage<T> retryMessage) {
        boolean b = super.processMessageFail(retryMessage);
        if (!b) {
            tLevelDbStoreServiceError.add(retryMessage);
            log.info("【失败重试组件】,加入异常队列", retryMessage.getId());
        }

        return b;
    }
}
