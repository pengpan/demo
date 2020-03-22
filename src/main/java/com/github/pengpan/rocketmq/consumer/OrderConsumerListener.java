package com.github.pengpan.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(topic = "my-topic-order", consumerGroup = "my-group-order")
public class OrderConsumerListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    if (msg != null) {
                        String message = new String(msg.getBody());
                        log.info("Get Order Message: {}", message);
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                }
                return null;
            }
        });
    }
}
