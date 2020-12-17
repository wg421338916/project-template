package com.aegis.template.demo.disruptor;

/**
 * 汽车信息
 *
 * @author Administrator
 */
public class MyInParkingDataEvent {

  /**
   * 车牌号
   */
  private String carLicense;

  public String getCarLicense() {
    return carLicense;
  }

  public void setCarLicense(String carLicense) {
    this.carLicense = carLicense;
  }
}