package com.wanggang.template.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域，参考：
 * https://blog.csdn.net/vincent_ling/article/details/51714691
 * https://blog.csdn.net/weixin_42036952/article/details/88564647
 *
 * @author 王刚
 * @version 1.0
 * @description mvc配置
 * @date 2019/12/27
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${wanggang.cors.origins}")
    private String allowedOrigins;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                //允许跨域的域名，可以用*表示允许任何域名使用
                        allowedOrigins(allowedOrigins).
                //允许任何方法（post、get等）
                        allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS").
                //允许任何请求头
                        allowedHeaders("*").
                //带上cookie信息
                        allowCredentials(true).
                exposedHeaders(HttpHeaders.SET_COOKIE).
                //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                        maxAge(3600L);
    }
}
