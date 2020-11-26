package com.aegis.template.service.simple;

import cn.hutool.core.util.StrUtil;
import com.aegis.template.commons.config.RabbitMqConfig;
import com.aegis.template.model.ContentWrapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 发送消息类
 *
 * @author wg
 * @version 1.0
 * @date 2020/06/09 2:37 下午
 * @since 1.0
 */
@Service
public class Sender {
  private final RabbitTemplate rabbitTemplate;

  public Sender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendTopicMessage(String content, String routeKey, String id) {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
    messageProperties.setHeader("__TypeId__", "java.lang.String");
    messageProperties.setContentType("application/json");
    Message message = new Message(StrUtil.bytes(content), messageProperties);

    rabbitTemplate.convertAndSend(RabbitMqConfig.DEMO_TOPIC, routeKey, content, new CorrelationData(id));
  }

  public void sendDirectMessage(String content, String routeKey, String id) {
    rabbitTemplate.convertAndSend(RabbitMqConfig.DEMO_DIRECT, routeKey, content, new CorrelationData(id));
  }

  public void sendFanoutMessage(String content, String id, Integer type) {
    ContentWrapper contentWrapper = new ContentWrapper();
    contentWrapper.setType(type);
    contentWrapper.setData(content);
    rabbitTemplate.convertAndSend(RabbitMqConfig.DEMO_FANOUT, contentWrapper, new CorrelationData(id));
  }
}
