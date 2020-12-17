package com.aegis.template.demo.disruptor;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.TimeUnit;

/**
 * Handler 第一个消费者，负责保存进场汽车的信息
 *
 * @author Administrator
 */
public class MyParkingDataInDbHandler implements EventHandler<MyInParkingDataEvent>, WorkHandler<MyInParkingDataEvent> {

  @Override
  public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch)
      throws Exception {
    this.onEvent(myInParkingDataEvent);
  }

  @Override
  public void onEvent(MyInParkingDataEvent myInParkingDataEvent) throws Exception {
    long threadId = Thread.currentThread().getId();
    String carLicense = myInParkingDataEvent.getCarLicense();

    TimeUnit.SECONDS.sleep(10);

    System.out.println(String.format("Thread Id %s 保存 %s 到数据库中 ....", threadId, carLicense));
  }
}
