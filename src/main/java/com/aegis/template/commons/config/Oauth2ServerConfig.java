package com.aegis.template.commons.config;

import com.aegis.template.service.impl.Oauth2ClientDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


/**
 * oauth2 验证
 * <p>
 * http://127.0.0.1:8080/oauth/token?grant_type=client_credentials&scope=select&client_id=client_1&client_secret=123456
 * 返回： {"access_token":"f981061a-945e-449c-8902-e8d732f24cae","token_type":"bearer","expires_in":41265,"scope":"select"}
 *
 * 登录方式：127.0.0.1:8080/oauth/token?grant_type=client_credentials&scope=select&client_id=aegis&client_secret=6543er1!
 * 数据库密码加密方式：BCryptPasswordEncoder，如果添加新密码，到测试类加密后添加到数据库
 * <p>
 * 请求资源：
 * http://127.0.0.1:8081/api/v1/order/1?access_token=f981061a-945e-449c-8902-e8d732f24cae
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 17:41
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Configuration
public class Oauth2ServerConfig {

  private static final String DEMO_RESOURCE_ID = "order";

  @Bean
  public PasswordEncoder passwordEncoder() {
    DelegatingPasswordEncoder delegatingPasswordEncoder =
        (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();

    //设置defaultPasswordEncoderForMatches为NoOpPasswordEncoder
    delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new PasswordEncoder() {
      @Override
      public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
      }
    });
    return delegatingPasswordEncoder;
  }


  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private Oauth2ClientDetailsServiceImpl clientDetailsService;
    private TokenStore tokenStore;

    public AuthorizationServerConfiguration(AuthenticationManager authenticationManager, DataSource dataSource, Oauth2ClientDetailsServiceImpl clientDetailsService) {
      this.authenticationManager = authenticationManager;
      this.tokenStore = new JdbcTokenStore(dataSource);
      this.clientDetailsService = clientDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
      //允许表单认证
      oauthServer.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
      endpoints
          .tokenStore(tokenStore)
          .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
          .authenticationManager(authenticationManager);
    }
  }

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {
      http
          .requestMatchers()
          .antMatchers("/api/**")
          .and()
          .authorizeRequests()
          .antMatchers("/api/**")
          .authenticated();

    }
  }
}
