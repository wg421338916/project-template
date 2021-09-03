package com.wanggang.template;

import cn.hutool.core.lang.Assert;
import com.wanggang.template.commons.utils.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * JsonTest
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/20 19:32
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.8
 */
public class JsonTest {

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", include = JsonTypeInfo.As.PROPERTY)
    public static class TestA {
        private String type;
    }

    @Data
    public static class TestB extends TestA implements Serializable {
        private String b;
        private LocalDateTime lt;
        private TestA test;
    }

    public static class TestC<T> extends TestA {
        private String c;

        private T test;

        public TestC() {
        }

        public TestC(T test) {
            this.test = test;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

    @Test
    public void testIt() {
        TestB b = new TestB();
        b.setB("b");
        b.setType("b");

        String s = JacksonUtil.object2Json(b);

        TestA test = JacksonUtil.json2Object(s, TestA.class);

        Assert.notNull(test);

        switch (test.getType()) {
            case "b":
                TestB b2 = (TestB) test;
                System.out.println(b2.getB());
                break;
            case "c":
                TestC c2 = (TestC) test;
                System.out.println(c2.getC());
                break;
        }

        Assert.notNull(test);
    }

    @Test
    public void testMoreThread() throws InterruptedException {
        IntStream.range(0, 100).forEach(i -> {
            new Thread(() -> {
                TestB b = new TestB();
                b.setB("b" + i);
                b.setType("b");

                String s = JacksonUtil.object2Json(b);

                System.out.println(s);

                TestA test = JacksonUtil.json2Object(s, TestA.class);

                Assert.notNull(test);
            }).start();
        });

        TimeUnit.SECONDS.sleep(2);
    }
}
