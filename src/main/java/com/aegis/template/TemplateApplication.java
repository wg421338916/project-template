package com.aegis.template;

import com.aegis.template.commons.config.GlobalConfig;
import com.aegis.template.commons.utils.retry.annotation.EnableRetrySync;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import java.time.LocalDateTime;

/**
 * 系统入口类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@EnableRetrySync
@SpringBootApplication
@EnableAdminServer
@Slf4j
@DependsOn({"applicationContextUtils","redisTemplate","ehCacheCacheManager"})
public class TemplateApplication {

  /**
   * 优先注入全局配置 GlobalConfig,否则可能会引起未知异常
   *
   * @param config
   */
  public TemplateApplication(GlobalConfig config) {
    log.info("程序启动,启动时间:{},{}", LocalDateTime.now(), config.toString());
  }

  public static void main(String[] args) {
    SpringApplication.run(TemplateApplication.class, args);
  }

}
