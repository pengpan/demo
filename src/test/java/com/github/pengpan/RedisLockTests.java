package com.github.pengpan;

import com.crossoverjie.distributed.lock.RedisLock;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@Slf4j
@SpringBootTest
public class RedisLockTests {

    private static ExecutorService executorServicePool;

    @Autowired
    private RedisLock redisLock;

    @Test
    public void lockTest() throws InterruptedException {
        initThread();

        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(new Worker(i));
        }

        executorServicePool.shutdown();
        while (!executorServicePool.awaitTermination(1, TimeUnit.SECONDS)) {
            log.info("worker running");
        }
        log.info("worker over");
    }

    public static void initThread() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("current-thread-%d").build();
        executorServicePool = new ThreadPoolExecutor(350, 350, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(200), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    }

    private class Worker implements Runnable {

        private int index;

        public Worker(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            //测试非阻塞锁
            boolean limit = redisLock.tryLock("abc", "12345");
            if (limit) {
                log.info("加锁成功=========");
                boolean unlock = redisLock.unlock("abc", "12345");
                log.info("解锁结果===[{}]", unlock);
            } else {
                log.info("加锁失败");

            }

            //测试非阻塞锁 + 超时时间
            //boolean limit = redisLock.tryLock("abc", "12345",1000);
            //if (limit) {
            //    logger.info("加锁成功=========");
            //    boolean unlock = redisLock.unlock("abc", "12345");
            //    logger.info("解锁结果===[{}]",unlock);
            //} else {
            //    logger.info("加锁失败");
            //
            //}


            //测试阻塞锁
            //try {
            //    redisLock.lock("abc", "12345");
            //    logger.info("加锁成功=========");
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
            //redisLock.unlock("abc","12345") ;


            //测试阻塞锁 + 阻塞时间
            //try {
            //    boolean limit = redisLock.lock("abc", "12345", 100);
            //    if (limit) {
            //        logger.info("加锁成功=========");
            //        boolean unlock = redisLock.unlock("abc", "12345");
            //        logger.info("解锁结果===[{}]",unlock);
            //    } else {
            //        logger.info("加锁失败");
            //
            //    }
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
        }
    }

}
