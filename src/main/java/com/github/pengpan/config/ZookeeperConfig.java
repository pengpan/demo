package com.github.pengpan.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static org.apache.zookeeper.Watcher.Event.KeeperState.SyncConnected;

@Slf4j
@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int sessionTimeout;

    @Bean
    public ZooKeeper zooKeeper() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, watchedEvent -> {
                if (SyncConnected == watchedEvent.getState()) {
                    log.info("init zookeeper success");
                }
            });
        } catch (IOException e) {
            log.error("init zookeeper error");
        }
        return zooKeeper;
    }

}
