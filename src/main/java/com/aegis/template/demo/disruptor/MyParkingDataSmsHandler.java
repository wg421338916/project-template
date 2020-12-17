package com.aegis.template.demo.disruptor;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * 第三个消费者，sms短信服务，告知司机你已经进入停车场，计费开始。
 *
 * @author Administrator
 */
public class MyParkingDataSmsHandler implements EventHandler<MyInParkingDataEvent> {

  @Override
  public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch) throws InterruptedException {
    long threadId = Thread.currentThread().getId();
    String carLicense = myInParkingDataEvent.getCarLicense();

    TimeUnit.SECONDS.sleep(10);

    System.out.println(String.format("Thread Id %s 给  %s 的车主发送一条短信，并告知他计费开始了 ....", threadId, carLicense));
  }
}
