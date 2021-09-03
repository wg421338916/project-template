package com.wanggang.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * LogTest
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/11 15:45
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.8
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("pro")
public class LogTest {

    @Test
    public void log() {
        log.error("error");
        log.info("info");
        log.debug("debug");
        log.warn("warn");
        log.trace("trace");

        Assert.assertTrue(true);
    }
}
