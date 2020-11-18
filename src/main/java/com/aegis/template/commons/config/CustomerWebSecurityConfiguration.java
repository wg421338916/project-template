package com.aegis.template.commons.config;

import com.aegis.template.commons.constants.SecurityLoginConstants;
import com.aegis.template.commons.filters.LoginFilter;
import com.aegis.template.commons.interceptor.handler.CustomerAccessDeniedHandler;
import com.aegis.template.commons.interceptor.handler.CustomerAuthLoginFailureHandler;
import com.aegis.template.commons.interceptor.handler.CustomerAuthLoginSuccessHandler;
import com.aegis.template.commons.interceptor.handler.CustomerAuthNoLoginHandler;
import com.aegis.template.commons.interceptor.provider.CaptchaAuthenticationProvider;
import com.aegis.template.commons.interceptor.provider.EmailAuthenticationProvider;
import com.aegis.template.commons.interceptor.provider.LogAuthenticationProvider;
import com.aegis.template.commons.interceptor.provider.SmsCodeAuthenticationProvider;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * spring Security study
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 11:34
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomerWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * 记住me7天
   */
  private static final int DAYS = 7;
  private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  private DataSource dataSource;
  @Value("${spring.boot.admin.client.password}")
  private String password;
  private UserDetailsService userDetailsService;
  @Value("${spring.boot.admin.client.username}")
  private String userName;

  public CustomerWebSecurityConfiguration(DataSource dataSource, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
    this.dataSource = dataSource;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers("/extensions/**", "/about/**", "/applications/**", "/instances/**", "/journal/**", "/wallboard/**", "/external/**").authenticated()
        .antMatchers("/actuator/**").authenticated()
        .antMatchers("/web/**").authenticated()
        .anyRequest().permitAll()
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomerAuthNoLoginHandler()).accessDeniedHandler(new CustomerAccessDeniedHandler())
        .and()
        .formLogin().loginPage("/login")
        .and()
        .httpBasic()
        .and()
        .addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  private Filter getFilter() throws Exception {
    CaptchaAuthenticationProvider captchaAuthenticationProvider = new CaptchaAuthenticationProvider();
    captchaAuthenticationProvider.setUserDetailsService(userDetailsService);
    captchaAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

    SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
    smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

    EmailAuthenticationProvider emailAuthenticationProvider = new EmailAuthenticationProvider();
    emailAuthenticationProvider.setUserDetailsService(userDetailsService);

    LogAuthenticationProvider logAuthenticationProvider = new LogAuthenticationProvider();

    ProviderManager manage = new ProviderManager(Lists.newArrayList(captchaAuthenticationProvider, emailAuthenticationProvider, smsCodeAuthenticationProvider, logAuthenticationProvider), super.authenticationManagerBean());


    LoginFilter loginFilter = new LoginFilter("/loginSys");
    loginFilter.setAuthenticationManager(manage);
    loginFilter.setAuthenticationSuccessHandler(new CustomerAuthLoginSuccessHandler());
    loginFilter.setAuthenticationFailureHandler(new CustomerAuthLoginFailureHandler());

    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);

    AbstractRememberMeServices tokenBasedRememberMeServices = new PersistentTokenBasedRememberMeServices("aegis-session", userDetailsService, tokenRepository);
    tokenBasedRememberMeServices.setParameter(SecurityLoginConstants.REMEMBER_ME);
    tokenBasedRememberMeServices.setTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(DAYS));
    loginFilter.setRememberMeServices(tokenBasedRememberMeServices);
    return loginFilter;
  }

  /**
   * 认证信息管理
   * spring5中摒弃了原有的密码存储格式，官方把spring security的密码存储格式改了
   *
   * @param auth
   * @throws Exception
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication() //认证信息存储到内存中
        .passwordEncoder(bCryptPasswordEncoder)
        .withUser(userName).password(bCryptPasswordEncoder.encode(password)).roles("ADMIN");
  }
}
