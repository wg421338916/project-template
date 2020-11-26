package com.aegis.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统入口类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@SpringBootApplication
@Slf4j
public class TemplateApplication {
  public static void main(String[] args) {
    SpringApplication.run(TemplateApplication.class, args);
  }
}
