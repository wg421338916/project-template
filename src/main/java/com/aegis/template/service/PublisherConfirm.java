package com.aegis.template.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 系统入口类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Service
public class PublisherConfirm implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
  public PublisherConfirm(RabbitTemplate rabbitTemplate) {
    rabbitTemplate.setReturnCallback(this);
    rabbitTemplate.setConfirmCallback(this);
  }

  /**
   * 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，
   * 也就是只确认是否正确到达 Exchange 中
   *
   * @param correlationData
   * @param ack
   * @param cause
   */
  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    System.out.println("id:" + correlationData.getId() + ",ack:" + ack);
  }

  /**
   * 通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
   *
   * @param message
   * @param replyCode
   * @param replyText
   * @param exchange
   * @param routingKey
   */
  @Override
  public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    System.out.println(new String(message.getBody()) + "no:" + replyCode);
  }
}
