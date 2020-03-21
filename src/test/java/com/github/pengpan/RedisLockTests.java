package com.github.pengpan;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class RedisLockTests {

    @Autowired
    private RedissonClient redisson;

    @Test
    public void lockTest() {
        ExecutorService pool = Executors.newFixedThreadPool(9);
        CountDownLatch countDownLatch = new CountDownLatch(9);
        for (int i = 0; i < 9; i++) {
            pool.execute(new Task(i));
            countDownLatch.countDown();
        }

        try {
            countDownLatch.await();
            pool.shutdown();
            while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class Task implements Runnable {

        private int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public void run() {
//            String lockKey = "LOCK_KEY";
            String lockKey = Thread.currentThread().getName();
            RLock rLock = redisson.getLock(lockKey);
            try {
                rLock.lock(2, TimeUnit.SECONDS);
                log.info(Thread.currentThread().getName() + "\t " + i);
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        }
    }

}
