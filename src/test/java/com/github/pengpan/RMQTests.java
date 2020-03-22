package com.github.pengpan;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class RMQTests {

    private final DefaultMQProducer producer;

    public RMQTests(@Autowired RocketMQTemplate rocketMQTemplate) {
        this.producer = rocketMQTemplate.getProducer();
    }

    @Test
    public void rmqTest() throws Exception {
        producer();
        orderProducer();
        transactionProducer();

        // waiting consumer...
        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * 发送普通消息
     */
    private void producer() {
        String msg = LocalDateTime.now().toString();

        Message message = new Message();
        message.setKeys("my-key");
        message.setTopic("my-topic");
        message.setTags("my-tag");
        message.setBody(msg.getBytes());

        try {
            SendResult result = producer.send(message);
            if (SendStatus.SEND_OK == result.getSendStatus()) {
                log.info("Send Message: {}", msg);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 发送顺序消息
     */
    private void orderProducer() {
        String msg = LocalDateTime.now().toString();

        Message message = new Message();
        message.setKeys("my-key-order");
        message.setTopic("my-topic-order");
        message.setTags("my-tag-order");
        message.setBody(msg.getBytes());

        try {
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    return mqs.get((int) arg);
                }
            }, 0);
            if (SendStatus.SEND_OK == result.getSendStatus()) {
                log.info("Send Order Message: {}", msg);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 发送事务消息
     */
    private void transactionProducer() {
        String msg = LocalDateTime.now().toString();

        Message message = new Message();
        message.setKeys("my-key-transaction");
        message.setTopic("my-topic-transaction");
        message.setTags("my-tag-transaction");
        message.setBody(msg.getBytes());

        try {
            TransactionSendResult result = producer.sendMessageInTransaction(message, null);
            if (SendStatus.SEND_OK == result.getSendStatus()) {
                log.info("Send Transaction Message: {}", msg);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
