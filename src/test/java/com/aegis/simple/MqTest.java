package com.aegis.simple;

import com.aegis.template.TemplateApplication;
import com.aegis.template.service.simple.Receive;
import com.aegis.template.service.simple.Sender;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = TemplateApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MqTest {

  @Autowired
  private Receive receive;
  @Autowired
  private Sender sender;

  @Test
  public void setSender() throws Exception {
    sender.sendTopicMessage("topic message", "a.q1", "send to q1 with topic");
    sender.sendTopicMessage("topic message", "a.q2", "send to q2 with topic");

    sender.sendDirectMessage("direct message", "q2", "send to q2 with direct");
    sender.sendDirectMessage("direct message", "q3", "send to q3 with direct");

    sender.sendFanoutMessage("fanout message", "send to  temp queue with fanout",1);
  }

  @Test
  public void receiverTopicQ1() throws Exception {
    sender.sendTopicMessage("topic message", "a.q1", "send to q1 with topic");

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    Assert.assertTrue(receive.getSets().contains("topic message"));

  }

  @Test
  public void receiverTopicQ2() throws Exception {
    sender.sendTopicMessage("topic message", "a.q2", "send to q2 with topic");

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    Assert.assertTrue(receive.getSets().contains("topic message"));
  }

  @Test
  public void receiverDirectQ2() throws Exception {
    sender.sendDirectMessage("direct message", "q2", "send to q2 with direct");

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    Assert.assertTrue(receive.getSets().contains("direct message"));
  }

  @Test
  public void receiverDirectQ3() throws Exception {
    sender.sendDirectMessage("direct message", "q3", "send to q3 with direct");

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    Assert.assertTrue(receive.getSets().contains("direct message"));
  }

  @Test
  public void receiverFanout() throws Exception {
    sender.sendFanoutMessage("fanout message", "send to tmp queue with fanout",1);

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    receive.resetLatch();

    sender.sendFanoutMessage("fanout message", "send to tmp queue with fanout",2);

    receive.getLatch().await(10000, TimeUnit.MILLISECONDS);

    Assert.assertTrue(receive.getSets().contains("fanout message"));
  }
}
