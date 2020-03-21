package com.github.pengpan.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
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

    @Scheduled(cron = "*/1 * * * * ?")
    public void producer() {

        DefaultMQProducer producer = rocketMQTemplate.getProducer();

        String msg = LocalDateTime.now().toString();

        Message message = new Message();
        message.setKeys("my-key");
        message.setTopic("my-topic");
        message.setTags("my-tag");
        message.setBody(msg.getBytes());

        try {
            producer.send(message);
            log.info("Send Message: {}", msg);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
