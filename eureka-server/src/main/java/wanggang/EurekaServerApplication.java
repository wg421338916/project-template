package wanggang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * https://www.cnblogs.com/yxth/p/10845640.html
 *
 * @author Administrator
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
  }
}