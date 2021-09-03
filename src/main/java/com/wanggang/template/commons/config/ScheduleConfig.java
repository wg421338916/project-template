package com.wanggang.template.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * ScheduleConfig 配置
 * 参考：https://www.cnblogs.com/skychenjiajun/p/9057379.html?utm_source=tuicool&utm_medium=referral
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    private static final int CORE_POOL_SIZE = 20;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService taskExecutor() {
        return new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
    }
}
