package com.aegis.template.commons.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 配置
 * http://192.168.10.7:15672/#/
 *
 * @author wg
 * @version 1.0
 * @date 2020/06/09 2:37 下午
 * @since 1.0
 */
@EnableRabbit
@Configuration
public class RabbitMqConfig {
  public static final String DEMO_DIRECT = "demo-direct";
  public static final String DEMO_FANOUT = "demo-fanout";
  public static final String DEMO_Q1 = "demo-q1";
  public static final String DEMO_Q2 = "demo-q2";
  public static final String DEMO_Q3 = "demo-q3";
  public static final String DEMO_TMP = "demo-tmp";
  public static final String DEMO_ROUTE_DIRECT_Q2 = "q2";
  public static final String DEMO_ROUTE_DIRECT_Q3 = "q3";
  public static final String DEMO_ROUTE_TOPIC_Q1 = "*.q1";
  public static final String DEMO_ROUTE_TOPIC_Q2 = "*.q2";
  public static final String DEMO_TOPIC = "demo-topic";

  /**
   * 定义消息转换器
   * @return
   */
  @Bean
  public MessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
  }

  //region 交换机

  /**
   * 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配
   *
   * @return
   */
  @Bean
  DirectExchange demoDirectExchange() {
    return new DirectExchange(DEMO_DIRECT, true, false);
  }

  /**
   * 它会把所有发送到该交换器的消息路由到所有与该交换器绑定的队列中
   *
   * @return
   */
  @Bean
  FanoutExchange demoFanoutExchange() {
    //非持久化，自动删除
    return new FanoutExchange(DEMO_FANOUT, false, true);
  }

  /**
   * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词
   *
   * @return
   */
  @Bean
  TopicExchange demoTopicExchange() {
    //非持久化，自动删除
    return new TopicExchange(DEMO_TOPIC, false, true);
  }
  //endregion

  //region 队列

  /**
   * 队列1，非持久化，自动删除
   *
   * @return
   */
  @Bean
  Queue demoQ1() {
    return new Queue(DEMO_Q1, false, false, true);
  }

  /**
   * 队列2，非持久化，自动删除
   *
   * @return
   */
  @Bean
  Queue demoQ2() {
    return new Queue(DEMO_Q2, true, false, false);
  }

  /**
   * 队列3，持久化，不自动删除
   *
   * @return
   */
  @Bean
  Queue demoQ3() {
    return new Queue(DEMO_Q3, true, false, false);
  }

  //endregion

  //region 绑定

  @Bean
  Binding directBindingQ3(Queue demoQ3, DirectExchange demoDirectExchange) {
    return BindingBuilder.bind(demoQ3).to(demoDirectExchange).with(DEMO_ROUTE_DIRECT_Q3);
  }

  @Bean
  Binding directBindingQ2(Queue demoQ2, DirectExchange demoDirectExchange) {
    return BindingBuilder.bind(demoQ2).to(demoDirectExchange).with(DEMO_ROUTE_DIRECT_Q2);
  }

  @Bean
  Binding topicBindingQ1(Queue demoQ1, TopicExchange demoTopicExchange) {
    return BindingBuilder.bind(demoQ1).to(demoTopicExchange).with(DEMO_ROUTE_TOPIC_Q1);
  }

  @Bean
  Binding topicBindingQ2(Queue demoQ2, TopicExchange demoTopicExchange) {
    return BindingBuilder.bind(demoQ2).to(demoTopicExchange).with(DEMO_ROUTE_TOPIC_Q2);
  }

  //endregion
}
