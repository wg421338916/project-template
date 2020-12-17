package com.aegis.template.demo.disruptor;


import com.lmax.disruptor.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * 第二个消费者，负责发送通知告知工作人员(Kafka是一种高吞吐量的分布式发布订阅消息系统)
 *
 * @author Administrator
 */
public class MyParkingDataToKafkaHandler implements EventHandler<MyInParkingDataEvent> {
  @Override
  public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch) throws InterruptedException {
    long threadId = Thread.currentThread().getId();
    String carLicense = myInParkingDataEvent.getCarLicense();

    TimeUnit.SECONDS.sleep(10);

    System.out.println(String.format("Thread Id %s 发送 %s 进入停车场信息给 kafka系统...", threadId, carLicense));
  }
}
