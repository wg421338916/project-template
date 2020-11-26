package com.aegis.template.service.simple;

import com.aegis.template.commons.config.RabbitMqConfig;
import com.aegis.template.model.ContentWrapper;
import lombok.Getter;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 导出工具类
 *
 * @author wg
 * @version 1.0
 * @date 2020/06/09 2:37 下午
 * @since 1.0
 */
@Component
public class Receive {

  private CountDownLatch latch = new CountDownLatch(1);
  @Getter
  private Set<String> sets = new HashSet<>();

  public CountDownLatch getLatch() {
    latch = new CountDownLatch(1);
    return latch;
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

  /**
   * q2
   *
   * @param message
   * @RabbitListener(bindings = @ QueueBinding (
   * exchange = @ Exchange ( value = RabbitMqConfig.DEMO_TOPIC, type = ExchangeTypes.TOPIC, durable = Exchange.FALSE, autoDelete = Exchange.TRUE),
   * value = @Queue(value = RabbitMqConfig.DEMO_Q2, durable = Exchange.TRUE, autoDelete = Exchange.FALSE),
   * key = RabbitMqConfig.DEMO_ROUTE_TOPIC_Q2))
   */
  @RabbitListener(queues = RabbitMqConfig.DEMO_Q2)
  public void receiveTopicQ2Message(String message) {
    System.out.println("Received <" + message + ">");

    sets.add(message);
    latch.countDown();
  }

  /**
   * topic q1
   *
   * @param message
   */
  @RabbitListener
      (
          bindings = @QueueBinding(
              exchange = @Exchange(value = RabbitMqConfig.DEMO_TOPIC, type = ExchangeTypes.TOPIC, durable = Exchange.FALSE, autoDelete = Exchange.TRUE),
              value = @Queue(value = RabbitMqConfig.DEMO_Q1, durable = Exchange.FALSE, autoDelete = Exchange.TRUE),
              key = RabbitMqConfig.DEMO_ROUTE_TOPIC_Q1
          )
      )
  public void receiveTopicQ1Message(String message) {
    System.out.println("Received <" + message + ">");

    sets.add(message);
    latch.countDown();
  }

  /**
   * DIRECT q3
   *
   * @param message
   */
  @RabbitListener
      (
          bindings = @QueueBinding(
              exchange = @Exchange(value = RabbitMqConfig.DEMO_DIRECT, type = ExchangeTypes.DIRECT, durable = Exchange.TRUE, autoDelete = Exchange.FALSE),
              value = @Queue(value = RabbitMqConfig.DEMO_Q3, durable = Exchange.TRUE, autoDelete = Exchange.FALSE),
              key = RabbitMqConfig.DEMO_ROUTE_DIRECT_Q3
          )
      )
  public void receiveDirectQ3Message(String message) {
    System.out.println("Received <" + message + ">");

    sets.add(message);
    latch.countDown();
  }


  /**
   * DIRECT tmp queue
   *
   * @param message
   */
  @RabbitListener
      (
          bindings = @QueueBinding(
              exchange = @Exchange(value = RabbitMqConfig.DEMO_FANOUT, type = ExchangeTypes.FANOUT, durable = Exchange.FALSE, autoDelete = Exchange.TRUE),
              value = @Queue
          )
      )
  public void receiveFanoutTMPMessage(ContentWrapper message) {
    System.out.println("Received <" + message.getData() + ">");

    sets.add(message.getData());
    latch.countDown();
  }
}
