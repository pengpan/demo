package com.github.pengpan.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "my-topic", consumerGroup = "my-group")
public class ConsumerListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("Get Message: {}", message);
    }
}
