package com.wanggang.template;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DisruptorTest
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/23 18:30
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class DisruptorTest {

    @Test(expected = IllegalArgumentException.class)
    public void test() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        AiEventFactory factory = new AiEventFactory();
        Disruptor<AiEvent> disruptor = new Disruptor<AiEvent>(factory, 1024, executor, ProducerType.MULTI, new TimeoutBlockingWaitStrategy(5, TimeUnit.SECONDS));
        disruptor.setDefaultExceptionHandler(new UKeFuExceptionHandler());
        disruptor.handleEventsWith(new UserEventHandler());
        disruptor.start();

        AtomicInteger a = new AtomicInteger();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        long seq = disruptor.getRingBuffer().next();
                        disruptor.getRingBuffer().get(seq).setId(1, a);
                        disruptor.getRingBuffer().publish(seq);
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("sssssss" + LocalDateTime.now() + "--" + a.incrementAndGet());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        TimeUnit.MINUTES.sleep(40);
    }

    public static class UserEventHandler implements EventHandler<AiEvent> {
        private static AtomicInteger a = new AtomicInteger();

        @Override
        public void onEvent(AiEvent arg0, long arg1, boolean arg2)
                throws Exception {
            log.error("test" + a.incrementAndGet() + "--" + arg0.print());

            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    public class AiEvent {
        private AtomicInteger a;
        private long id;

        public long getId() {
            return id;
        }

        public int print() {
            int i = a.get();
            return i;

        }

        public void setId(long id, AtomicInteger a) {
            this.id = id;
            this.a = a;
        }
    }

    public class AiEventFactory implements EventFactory<AiEvent> {

        @Override
        public AiEvent newInstance() {
            return new AiEvent();
        }
    }

    public class UKeFuExceptionHandler implements ExceptionHandler<Object> {

        @Override
        public void handleEventException(Throwable ex, long arg1, Object arg2) {
            ex.printStackTrace();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            // TODO Auto-generated method stub
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }
}
