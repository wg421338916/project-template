package com.wanggang.template.commons.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring Druid study
 * 参考:https://www.cnblogs.com/lijiasnong/p/9889510.html
 * 访问：http://ip:port/druid/login.html
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 11:34
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Configuration
public class DruidConfiguration {
    @Value("${spring.boot.admin.client.username}")
    private String userName;
    @Value("${spring.boot.admin.client.password}")
    private String password;

    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServle() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        //控制台用户
        servletRegistrationBean.addInitParameter("loginUsername", userName);
        servletRegistrationBean.addInitParameter("loginPassword", password);
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "true");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> statFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
