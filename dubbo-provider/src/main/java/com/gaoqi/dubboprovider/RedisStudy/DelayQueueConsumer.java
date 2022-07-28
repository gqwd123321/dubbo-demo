package com.gaoqi.dubboprovider.RedisStudy;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
@AllArgsConstructor
@Lazy(value = false)

public class DelayQueueConsumer {

    private RedisDelayQueue redisDelayQueue;

    @Async
//    @Scheduled(cron = "*/1 * * * * *")
    public void consumer(){
        List<RedisTaskItem> list = redisDelayQueue.pull();
        if(null!=list){
            long currentTime = System.currentTimeMillis();
            for(RedisTaskItem item:list){
                if(currentTime>=item.getDelayTime()){
                    log.info("消费消息：{}:消息创建时间：{},消费时间：{}", JSON.toJSONString(item), item.getCreateTime(), LocalDateTime.now());
                    System.out.println(item);
                    if(redisDelayQueue.remove(item)){
                        log.info("任务已经取出");
                    };
                }
            }
        }
        log.info("目前没有任务");
    }
}
