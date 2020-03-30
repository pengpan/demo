package com.github.pengpan;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class ZookeeperTests {

    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void zookeeperTests() throws Exception {
        List<String> children = zooKeeper.getChildren("/", false);
        children.forEach(System.out::println);
    }
}
