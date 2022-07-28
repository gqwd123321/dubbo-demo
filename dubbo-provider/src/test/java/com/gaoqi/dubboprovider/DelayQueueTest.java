package com.gaoqi.dubboprovider;

import com.gaoqi.dubboprovider.RedisStudy.DelayQueueProducter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
public class DelayQueueTest {


    @Autowired
    private DelayQueueProducter delayQueueProducter;

    @Test
    void dqTest(){
        delayQueueProducter.sentTaskMessage("任务1",10);
        delayQueueProducter.sentTaskMessage("任务2",10);
    }
}
