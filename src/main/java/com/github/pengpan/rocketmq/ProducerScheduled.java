package com.github.pengpan.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ProducerScheduled {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通消息
     */
    @Scheduled(cron = "0 *／1 * * * ?")
    public void producer() {
        DefaultMQProducer producer = rocketMQTemplate.getProducer();

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
     * 发送事务消息
     */
    @Scheduled(cron = "0 *／1 * * * ?")
    public void transactionProducer() {
        DefaultMQProducer producer = rocketMQTemplate.getProducer();

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
