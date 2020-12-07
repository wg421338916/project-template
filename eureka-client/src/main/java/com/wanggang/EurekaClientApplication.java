package com.wanggang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

/**
 * openfeign:https://blog.csdn.net/xiaoyudian_/article/details/104280878
 * openfeign: https://cloud.spring.io/spring-cloud-openfeign/reference/html/
 *
 * @author Administrator
 */

@EnableRetry
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaClientApplication.class, args);
  }
}