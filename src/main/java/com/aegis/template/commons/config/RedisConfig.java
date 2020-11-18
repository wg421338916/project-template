package com.aegis.template.commons.config;

import com.aegis.template.commons.constants.RedisKeyConstants;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 *
 * @author 王刚
 * @version 1.0
 * @date 2019/12/27
 * @since 1.0.0
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
  /**
   * redisTemplate相关配置
   *
   * @param factory
   * @return
   */
  @Bean
  public RedisTemplate<RedisKeyConstants, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<RedisKeyConstants, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    setRedisTemplate(redisTemplate);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  private void setRedisTemplate(RedisTemplate<RedisKeyConstants, Object> redisTemplate) {
    GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    // 设置值（value）的序列化采用FastJsonRedisSerializer。
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    // 设置键（key）的序列化采用StringRedisSerializer。
    redisTemplate.setKeySerializer(new RedisKeySerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
  }

  private static class RedisKeySerializer implements RedisSerializer<RedisKeyConstants> {
    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public byte[] serialize(RedisKeyConstants redisKeyConstants) {
      return stringRedisSerializer.serialize(redisKeyConstants.getKey());
    }

    @Override
    public RedisKeyConstants deserialize(byte[] bytes) {
      return null;
    }

    @Override
    public boolean canSerialize(Class<?> type) {
      return stringRedisSerializer.canSerialize(type);
    }

    @Override
    public Class<?> getTargetType() {
      return RedisKeyConstants.class;
    }
  }
}
