package com.aegis.template.commons.config;

import lombok.Data;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 参考:https://blog.csdn.net/caiwufei/article/details/78863804
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 17:21
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Configuration
@EnableCaching
public class CacheManagerConfig {

  @Bean
  @Primary
  public CacheManager cacheManager(RedisCacheManager redisCacheManager, EhCacheCacheManager ehCacheManager) {
    CustomerCacheManager cacheManager = new CustomerCacheManager();
    cacheManager.setRedisCacheManager(redisCacheManager);
    cacheManager.setEhCacheManager(ehCacheManager);
    return cacheManager;
  }

  @PreDestroy
  public void preDestroy() {
    ehCacheCacheManager().getCacheManager().shutdown();
  }

  @Bean
  public EhCacheCacheManager ehCacheCacheManager() {
    EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
    cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
    cacheManagerFactoryBean.setShared(true);
    return new EhCacheCacheManager(cacheManagerFactoryBean.getObject());
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    //初始化一个RedisCacheWriter
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    //设置CacheManager的值序列化方式为json序列化
    RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
        .fromSerializer(jsonSerializer);
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(pair).computePrefixWith(name -> name + ":");
    //初始化RedisCacheManager
    return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
  }

  @Data
  public class CustomerCacheManager implements CacheManager {
    private static final String PREFIX = "eh-";
    private CacheManager ehCacheManager;
    private CacheManager redisCacheManager;

    @Override
    public Cache getCache(String name) {
      if (name.startsWith(PREFIX)) {
        return ehCacheManager.getCache(name.substring(3));
      }

      return redisCacheManager.getCache(name);
    }

    @Override
    public Collection<String> getCacheNames() {
      Collection<String> cacheNames = new ArrayList<>();
      if (redisCacheManager != null) {
        cacheNames.addAll(redisCacheManager.getCacheNames());
      }
      if (ehCacheManager != null) {
        cacheNames.addAll(ehCacheManager.getCacheNames());
      }
      return cacheNames;
    }
  }
}
