package com.aegis.template.commons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig 配置
 * 参考：https://www.jianshu.com/p/60174c9eb735
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */

@Configuration
public class RestTemplateConfig {
  @Bean
  @ConfigurationProperties(prefix = "aegis.rest.connection")
  public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
    return new HttpComponentsClientHttpRequestFactory();
  }

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate(customHttpRequestFactory());
  }
}
