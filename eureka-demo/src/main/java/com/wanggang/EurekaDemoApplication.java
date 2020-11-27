package com.wanggang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka: https://www.cnblogs.com/yxth/p/10845640.html
 * Eureka: http://www.uml.org.cn/wfw/2019062521.asp
 * <p>
 * Spring Cloud Eureka 自我保护机制: https://www.cnblogs.com/xishuai/p/spring-cloud-eureka-safe.html
 * eureka服务事件监听: https://blog.csdn.net/qq_39506978/article/details/104739552
 * Region(区域)/zone(可用区) 原理解析: https://blog.csdn.net/zhuyanlin09/article/details/89598245
 * 注册中心 Eureka 集群是怎么保持数据一致的: https://zhuanlan.zhihu.com/p/96396725
 *
 * @author Administrator
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaDemoApplication.class, args);
  }
}