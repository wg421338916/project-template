package com.wanggang.template.service.impl;

import com.wanggang.template.commons.utils.retry.annotation.RetrySyncListener;
import com.wanggang.template.commons.utils.retry.annotation.RetrySyncPolicy;
import com.wanggang.template.commons.utils.retry.annotation.RetrySyncQueue;
import org.springframework.stereotype.Service;

/**
 * RetrySyncDemo demo
 * 1。必须开开启 @EnableRetrySync,一般在 Application 入口类上开启
 * 2。类上必须有 @RetrySyncListener 注解
 * 3。异步方法上必须包含 @RetrySyncQueue 注解，方法必须是运回Boolean类型及有一个参数
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 19:20
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Service
@RetrySyncListener
public class RetrySyncDemo {

    @RetrySyncQueue(name = "sayHello1")
    public boolean sayHello1(Object demo) {
        System.out.println(demo);
        return true;
    }

    @RetrySyncQueue(name = "sayHello2", delayMillis = 2000, isPersistent = false)
    public Boolean sayHello2(Object demo) {
        System.out.println(demo);
        return true;
    }

    @RetrySyncQueue(name = "sayHello3", policy = @RetrySyncPolicy(maxIntervalSec = 0, multiplier = 0, maxTryCount = 3))
    public boolean sayHello3(String demo) {
        System.out.println(demo);
        return false;
    }
}
